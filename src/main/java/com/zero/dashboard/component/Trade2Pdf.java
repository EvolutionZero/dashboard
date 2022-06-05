package com.zero.dashboard.component;

import cn.hutool.system.OsInfo;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Trade2Pdf {

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
            options.addArguments("--headless", "--no-sandbox", "--window-size=1920,1080");
        }
//        options.addArguments("--headless");
        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.get(url);
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        chromeDriver.manage().window().maximize();
        Actions actions = new Actions(chromeDriver);
        // 鼠标移动到一侧
        actions.moveByOffset(920, 0).perform();
        WebDriverWait wait = new WebDriverWait(chromeDriver,5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kline"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("volume"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("macd"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kdj"))).click();
        String command = "Page.printToPDF";
        Map<String, Object> params = new HashMap();
        params.put("width", "1920px");
        params.put("height", "1080px");
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
