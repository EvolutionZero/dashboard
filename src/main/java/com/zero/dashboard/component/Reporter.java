package com.zero.dashboard.component;


import cn.hutool.core.date.StopWatch;
import cn.hutool.system.OsInfo;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;
import com.zero.dashboard.enums.ScreenshotTypeEnum;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Reporter {

    @Resource(name = "chromes")
    private LinkedBlockingQueue<WebDriver> chromes;


    public ScreenshotResponse export(ScreenshotRequest request) {
        WebDriver driver = null;
        for (int i = 0; i < 10; i++) {
            try {
                driver = chromes.poll(100, TimeUnit.MILLISECONDS);
                if(driver != null){
                    break;
                }
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
        if(driver == null){
            throw new RuntimeException("无可用driver");
        }
        ScreenshotResponse response = new ScreenshotResponse();
        try {
            StopWatch stopWatch = new StopWatch();
            Map<String, Object> params = request.getParams();
            String fileName = request.getFileName();
            if(new OsInfo().isWindows()){
                params.put("scriptHomePath", "D:/IdeaProjects/dashboard/src/main/resources/velocity");
            } else {
                params.put("scriptHomePath", "/opt/selenium/opt/dashboard/script");
            }
            stopWatch.start("生成html");
            String html = toHtml(params);

            String fileHomePath = new OsInfo().isLinux() ? "/opt/selenium/opt/dashboard/report/" : "./";
            String htmlPath = fileHomePath + "html/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "_" + fileName + (fileName.endsWith(".html") ? "" : ".html");
            try {
                FileUtils.forceMkdir(new File(fileHomePath));
                FileUtils.writeStringToFile(new File(htmlPath), html, "UTF-8");
            } catch (IOException e) {
                log.error("", e);
            }
            stopWatch.stop();

            if(request.getTypes().contains(ScreenshotTypeEnum.HTML.getValue())){
                response.setHtmlPath(htmlPath);
            }
            stopWatch.start("生成图片");
            BufferedImage bufferedImage = null;
            if(request.getTypes().contains(ScreenshotTypeEnum.PNG.getValue())){
                String pngPath = fileHomePath + "png/" + fileName + ".png";
                bufferedImage = new TradeScreenshot().exec(driver, "file://" + htmlPath, pngPath);
                response.setPngPath(pngPath);
            }
            stopWatch.stop();

            stopWatch.start("上传图片");
            if(bufferedImage != null){
                uploadToMinio(bufferedImage, fileName + ".png");
            } else {
                uploadToMinio(response.getPngPath(), fileName + ".png");
            }
            stopWatch.stop();

            log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

        } finally {
            try {
                chromes.put(driver);
            } catch (InterruptedException e) {
                log.error("", e);
            }
        }
        //FIXME PDF截图有问题暂时不用
//        new Trade2Pdf().exec("file://" + htmlPath, fileHomePath + "pdf/" + fileName + ".pdf");
        return response;
    }

    public String toHtml(Map<String, Object> params) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        VelocityEngine engine = new VelocityEngine();
        engine.init(properties);
        String templateName = "velocity/reportTemplate.vm";

        StringWriter stringWriter = new StringWriter();
        Template template = engine.getTemplate(templateName, "UTF-8");
        VelocityContext context = new VelocityContext();
        params.forEach((k, v) -> context.put(k, v));

        template.merge(context, stringWriter);
        return stringWriter.toString();
    }


    private void uploadToMinio(String filePath, String objectName){
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://192.168.3.140:9000/")
                    .credentials("admin","admin123")
                    .build();
            String bucketName = "test";
            boolean found = minioClient.bucketExists(BucketExistsArgs.
                    builder().bucket(bucketName).build());
            if (!found){
                // 新建一个桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/233/" + objectName)
                            .filename(filePath) // 本地磁盘的路径
                            .build()
            );
            System.out.println("上传成功");

        }catch (Exception e){
            log.error("", e);
        }
    }

    private void uploadToMinio(BufferedImage bufferedImage, String objectName){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://192.168.3.140:9000/")
                    .credentials("admin","admin123")
                    .build();
            String bucketName = "test";
            boolean found = minioClient.bucketExists(BucketExistsArgs.
                    builder().bucket(bucketName).build());
            if (!found){
                // 新建一个桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object("/233/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "_" + objectName).stream(
                                    is, is.available(), -1)
                            .build());
            System.out.println("上传成功");

        }catch (Exception e){
            log.error("", e);
        }
    }


}
