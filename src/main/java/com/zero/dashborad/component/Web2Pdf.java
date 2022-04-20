package com.zero.dashborad.component;

import cn.hutool.system.OsInfo;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Web2Pdf {

    /**
     * 仅支持静态业务，动态加载业务有问题
     * @param url
     * @param filePath
     */
    public void exec(String url, String filePath){
        ChromeOptions options = new ChromeOptions();
        if(new OsInfo().isLinux()){
            //            options.setBinary("/opt/98.0.4758.102-google-chrome-stable_current_x86_64.rpm");
            options.setBinary("/opt/google/chrome/chrome");
            options.addArguments("--headless", "--no-sandbox");
        }
        options.addArguments("--headless");
        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.get(url);
        Map<String, Object> params = new HashMap();
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        chromeDriver.manage().window().maximize();
        String command = "Page.printToPDF";
        Map<String, Object> output = chromeDriver.executeCdpCommand(command, params);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byte[] byteArray = java.util.Base64.getDecoder().decode((String) output.get("data"));
            fileOutputStream.write(byteArray);
            fileOutputStream.close();
        } catch (IOException e) {
            log.error("", e);
        }
        chromeDriver.quit();
    }
}
