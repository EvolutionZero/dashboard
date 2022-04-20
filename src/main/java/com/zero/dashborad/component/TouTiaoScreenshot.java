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

public class TouTiaoScreenshot {

    private static final int HEADER_HEIGHT_PX = 64;

    public void exec(String url, String filePath){
        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true);
//        options.setBinary("D:\\develop\\chrome-driver\\98.0.4758.102_chrome64_stable_windows_installer.exe");
//        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "start-maximized", "disable-infobars");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        FixedHeaderViewportPastingDecorator shootingStrategy = (new FixedHeaderViewportPastingDecorator(new SimpleShootingStrategy(), HEADER_HEIGHT_PX));
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver,5);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("article-content"))).click();
        AShot aShot = new AShot()
                .shootingStrategy(shootingStrategy);
        Screenshot screenshot = aShot
                .takeScreenshot(driver);

        BufferedImage image = screenshot.getImage();
        try {
            ImageIO.write(image, "PNG", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.close();
    }

}
