package com.zero.dashboard.component;

import cn.hutool.system.OsInfo;
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
    }


    public void exec(String url, String filePath){
//        ChromeOptions options = new ChromeOptions();
//        if(new OsInfo().isLinux()){
//            options.setBinary("/opt/google/chrome/chrome");
//            options.addArguments("--headless", "--no-sandbox", "--window-size=1920,1080");
//        }
//        WebDriver driver = new ChromeDriver(options);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().window().maximize();
        DRIVER.get(url);
        Actions actions = new Actions(DRIVER);
        // 鼠标移动到一侧
        actions.moveByOffset(920, 0).perform();
        WebDriverWait wait = new WebDriverWait(DRIVER,5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kline"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("volume"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("macd"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("kdj"))).click();
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(DRIVER);
        BufferedImage image = screenshot.getImage();
        try {
            ImageIO.write(image, "PNG", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        driver.close();
    }
}
