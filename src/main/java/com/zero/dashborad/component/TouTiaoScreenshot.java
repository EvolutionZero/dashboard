package com.zero.dashborad.component;

import com.zero.dashborad.strategy.FixedHeaderViewportPastingDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

public class TouTiaoScreenshot {

    private static final int HEADER_HEIGHT_PX = 64;

    public void exec(){
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        FixedHeaderViewportPastingDecorator shootingStrategy = (new FixedHeaderViewportPastingDecorator(new SimpleShootingStrategy(), HEADER_HEIGHT_PX)).withScrollTimeout(100);
        driver.get("https://www.toutiao.com/article/7087963907754590757");
//        driver.get("https://www.toutiao.com/article/7088274288377233955");
        WebDriverWait wait = new WebDriverWait(driver,5);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("article-content"))).click();
        AShot aShot = new AShot()
                .shootingStrategy(shootingStrategy);
        Screenshot screenshot = aShot
                .takeScreenshot(driver);

        BufferedImage image = screenshot.getImage();
        try {
            ImageIO.write(image, "PNG", new File("D:\\temp\\" + "AShot_BBC_Entire_" + new Date().getTime() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.close();
    }

}
