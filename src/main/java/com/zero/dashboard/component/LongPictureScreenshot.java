package com.zero.dashboard.component;

import cn.hutool.system.OsInfo;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class LongPictureScreenshot {

    public void exec(String url, String filePath){
        ChromeOptions options = new ChromeOptions();
        if(new OsInfo().isLinux()){
            //            options.setBinary("/opt/98.0.4758.102-google-chrome-stable_current_x86_64.rpm");
            options.setBinary("/opt/google/chrome/chrome");
            options.addArguments("--headless", "--no-sandbox");
        }
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(url);
        new WebDriverWait(driver, 300000).until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
        BufferedImage image = screenshot.getImage();
        try {
            ImageIO.write(image, "PNG", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.close();
    }
}
