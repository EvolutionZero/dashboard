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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TradeScreenshot {

    private static final WebDriver DRIVER;

    static {
        ChromeOptions options = new ChromeOptions();
        if(new OsInfo().isLinux()){
            options.setBinary("/opt/google/chrome/chrome");
            options.addArguments("--headless", "--no-sandbox", "--window-size=1920,1080");
        }
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        DRIVER = driver;
        Actions actions = new Actions(DRIVER);
        actions.moveByOffset(920, 0).perform();
    }

    public BufferedImage exec(String url, String filePath){
        StopWatch stopWatch = new StopWatch();
//        stopWatch.start("启动浏览器");
//        ChromeOptions options = new ChromeOptions();
//        if(new OsInfo().isLinux()){
//            options.setBinary("/opt/google/chrome/chrome");
//            options.addArguments("--headless", "--no-sandbox", "--window-size=1920,1080");
//        }
//        WebDriver driver = new ChromeDriver(options);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().window().maximize();
//        stopWatch.stop();

        stopWatch.start("打开网页");
        DRIVER.get(url);
        stopWatch.stop();

//        stopWatch.start("开始动作");
//        Actions actions = new Actions(DRIVER);
//        stopWatch.stop();
//
//        // 鼠标移动到一侧
//        stopWatch.start("鼠标移动到一侧");
//        actions.moveByOffset(920, 0).perform();
//        stopWatch.stop();

        stopWatch.start("等待渲染完成");
//        WebDriverWait wait = new WebDriverWait(driver,5, 3);

        new WebDriverWait(DRIVER, 5, 3).until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));

//        wait.until(ExpectedConditions.elementToBeSelected(By.id("kline")));
//        wait.until(ExpectedConditions.elementToBeClickable(By.id("volume")));
//        wait.until(ExpectedConditions.elementToBeClickable(By.id("macd")));
//        wait.until(ExpectedConditions.elementToBeClickable(By.id("kdj")));

//        try {
//            Thread.sleep(50);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        stopWatch.stop();

        stopWatch.start("截图");
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(0)).takeScreenshot(DRIVER);
        stopWatch.stop();

        stopWatch.start("保存图片");
        BufferedImage image = screenshot.getImage();
//        try {
//            ImageIO.write(image, "PNG", new File(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        stopWatch.stop();

//        stopWatch.start("关闭浏览器");
//        driver.close();
//        stopWatch.stop();

        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return image;
    }
}
