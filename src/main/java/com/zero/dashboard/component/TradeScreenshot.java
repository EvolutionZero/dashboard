package com.zero.dashboard.component;

import cn.hutool.core.date.StopWatch;
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


import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TradeScreenshot {

    private static final WebDriver DRIVER;

    static {
        ChromeOptions options = new ChromeOptions();
        if(new OsInfo().isLinux()){
            options.setBinary("/opt/google/chrome/chrome");
            options.addArguments(
                    "--headless",
                    "--no-sandbox",
                    "--window-size=1920,1080" ,
                    "--disable-dev-shm-usage",
                    "--disk-cache-dir=/opt/selenium/opt/dashboard/cache"
            );
        }
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        DRIVER = driver;
        Actions actions = new Actions(DRIVER);
        actions.moveByOffset(920, 0).perform();
    }

    public BufferedImage exec(String url){
        StopWatch stopWatch = new StopWatch("截图流程");
        log.info("driver:" + DRIVER);
        stopWatch.start("打开网页");
        DRIVER.get(url);
        stopWatch.stop();

        stopWatch.start("等待渲染完成");
//        new WebDriverWait(DRIVER, 5, 3).until(d -> ((JavascriptExecutor) d)
//                .executeScript("return document.readyState").equals("complete"));
        WebDriverWait wait = new WebDriverWait(DRIVER,5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kline"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("volume"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("macd"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kdj"))).click();
        stopWatch.stop();

        stopWatch.start("截图");
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(0)).takeScreenshot(DRIVER);
        stopWatch.stop();

        BufferedImage image = screenshot.getImage();

        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return image;
    }
}
