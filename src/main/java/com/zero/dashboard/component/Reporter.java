package com.zero.dashboard.component;


import cn.hutool.system.OsInfo;
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

    public String export(Map<String, Object> params, String fileName) {
        String html = toHtml(params);
        String fileHomePath = new OsInfo().isLinux() ? "/opt/selenium/dashboard/" : "./";
        String baseDir = fileHomePath + "report/";
        String filePath = baseDir + fileName + (fileName.endsWith(".html") ? ".html" : "");
        try {
            FileUtils.forceMkdir(new File(baseDir));
            FileUtils.writeStringToFile(new File(filePath), html, "UTF-8");
        } catch (IOException e) {
            log.error("", e);
        }
        return filePath;
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
}
