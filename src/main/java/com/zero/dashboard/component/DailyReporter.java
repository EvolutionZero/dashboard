package com.zero.dashboard.component;


import cn.hutool.core.date.StopWatch;
import cn.hutool.system.OsInfo;
import com.zero.dashboard.dto.ctx.DailyReportContext;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 日线上报
 * @author Zero
 * @since 2022.11.12 16:07
 */
@Slf4j
@Component
public class DailyReporter {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.username}")
    private String minioUsernmae;

    @Value("${minio.password}")
    private String minioPassword;

    public void exec(DailyReportContext ctx) {

        String htmlPath = "";
        try {
            StopWatch stopWatch = new StopWatch();
            Map<String, Object> params = ctx.getParams();
            String fileName = ctx.getFileName();
            if(new OsInfo().isWindows()){
                params.put("scriptHomePath", "D:/IdeaProjects/dashboard/src/main/resources/velocity");
            } else {
                params.put("scriptHomePath", "/opt/selenium/opt/dashboard/script");
            }
            stopWatch.start("生成html");
            String html = toHtml(ctx.getTemplateName(), params);

            String fileHomePath = new OsInfo().isLinux() ? "/opt/selenium/opt/dashboard/report/" : "./";
            htmlPath = fileHomePath + "html/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "_" + fileName + (fileName.endsWith(".html") ? "" : ".html");
            try {
                FileUtils.forceMkdir(new File(fileHomePath));
                FileUtils.writeStringToFile(new File(htmlPath), html, "UTF-8");
            } catch (IOException e) {
                log.error("", e);
            }
            stopWatch.stop();

            stopWatch.start("生成图片");
            String pngPath = fileHomePath + "png/" + fileName + ".png";
            BufferedImage bufferedImage = new TradeScreenshot().exec("file://" + htmlPath);
            stopWatch.stop();

            stopWatch.start("上传图片");
            uploadToMinio(bufferedImage, ctx.getBucketName(), ctx.getObjectPath(), fileName + ".png");
            stopWatch.stop();

            log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

        } finally {
            FileUtils.deleteQuietly(new File(htmlPath));
        }

    }

    public String toHtml(String templateName, Map<String, Object> params) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        VelocityEngine engine = new VelocityEngine();
        engine.init(properties);

        StringWriter stringWriter = new StringWriter();
        Template template = engine.getTemplate(templateName, "UTF-8");
        VelocityContext context = new VelocityContext();
        params.forEach((k, v) -> context.put(k, v));

        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    private void uploadToMinio(BufferedImage bufferedImage, String bucketName, String objectPath, String objectName){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(minioUsernmae,minioPassword)
                    .build();
            boolean found = minioClient.bucketExists(BucketExistsArgs.
                    builder().bucket(bucketName).build());
            if (!found){
                // 新建一个桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            if(StringUtils.isEmpty(objectPath)){
                objectPath = "/";

            } else {
                objectPath = objectPath.startsWith("/") ? objectPath : "/" + objectPath;
                objectPath = objectPath.endsWith("/") ? objectPath : objectPath + "/";
            }
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(objectPath + objectName).stream(
                                    is, is.available(), -1)
                            .build());
            log.info("上传成功");

        }catch (Exception e){
            log.error("", e);
        }
    }

}
