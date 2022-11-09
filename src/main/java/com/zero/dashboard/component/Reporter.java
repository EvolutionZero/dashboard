package com.zero.dashboard.component;


import cn.hutool.system.OsInfo;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;
import com.zero.dashboard.enums.ScreenshotTypeEnum;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Reporter {

    public ScreenshotResponse export(ScreenshotRequest request) {
        Map<String, Object> params = request.getParams();
        String fileName = request.getFileName();
        if(new OsInfo().isWindows()){
            params.put("scriptHomePath", "D:/IdeaProjects/dashboard/src/main/resources/velocity");
        } else {
            params.put("scriptHomePath", "/opt/selenium/opt/dashboard/script");
        }
        String html = toHtml(params);
        String fileHomePath = new OsInfo().isLinux() ? "/opt/selenium/opt/dashboard/report/" : "./";
        String htmlPath = fileHomePath + "html/" + fileName + (fileName.endsWith(".html") ? "" : ".html");
        try {
            FileUtils.forceMkdir(new File(fileHomePath));
            FileUtils.writeStringToFile(new File(htmlPath), html, "UTF-8");
        } catch (IOException e) {
            log.error("", e);
        }
        ScreenshotResponse response = new ScreenshotResponse();
        if(request.getTypes().contains(ScreenshotTypeEnum.HTML.getValue())){
            response.setHtmlPath(htmlPath);
        }
        if(request.getTypes().contains(ScreenshotTypeEnum.PNG.getValue())){
            String pngPath = fileHomePath + "png/" + fileName + ".png";
            new TradeScreenshot().exec("file://" + htmlPath, pngPath);
            response.setPngPath(pngPath);
        }
        uploadToMinio(response.getPngPath(), fileName + ".png");
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
                            .object(objectName)
                            .filename(filePath) // 本地磁盘的路径
                            .build()
            );
            System.out.println("上传成功");

        }catch (Exception e){
            log.error("", e);
        }


    }
}
