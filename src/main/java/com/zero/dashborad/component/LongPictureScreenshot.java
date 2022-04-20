package com.zero.dashborad.component;

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
