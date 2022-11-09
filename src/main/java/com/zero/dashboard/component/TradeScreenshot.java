package com.zero.dashboard.component;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.OsInfo;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TradeScreenshot {

    public void exec(String url, String filePath){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("启动浏览器");
        ChromeOptions options = new ChromeOptions();
        if(new OsInfo().isLinux()){
            options.setBinary("/opt/google/chrome/chrome");
            options.addArguments("--headless", "--no-sandbox", "--window-size=1920,1080");
        }
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        stopWatch.stop();

        stopWatch.start("打开网页");
        driver.get(url);
        stopWatch.stop();

        stopWatch.start("等待加载完成");
        Actions actions = new Actions(driver);
        // 鼠标移动到一侧
        actions.moveByOffset(920, 0).perform();
        WebDriverWait wait = new WebDriverWait(driver,5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kline"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("volume"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("macd"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kdj"))).click();
        stopWatch.stop();

//        stopWatch.start("截图");
//        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
//        stopWatch.stop();
//
//        stopWatch.start("保存图片");
//        BufferedImage image = screenshot.getImage();
//        try {
//            ImageIO.write(image, "PNG", new File(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        stopWatch.stop();

//        stopWatch.start("关闭浏览器");
//        stopWatch.stop();

        stopWatch.start("命令截图");
        String command = "Page.captureScreenshot";
        Map<String, Object> params = new HashMap();
        params.put("width", "1920px");
        params.put("height", "1080px");
        params.put("fromSurface", true);
        Map<String, Object> output = driver.executeCdpCommand(command, params);
        log.info("命令截图: " + JSONUtil.toJsonStr(output));
        log.info("命令截图: " + JSONUtil.toJsonStr(output.keySet()));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byte[] byteArray = java.util.Base64.getDecoder().decode((String) output.get("data"));
            fileOutputStream.write(byteArray);
            fileOutputStream.close();
        } catch (IOException e) {
            log.error("", e);
        }
        stopWatch.stop();

        driver.close();

        log.info(stopWatch.prettyPrint());
    }
}
