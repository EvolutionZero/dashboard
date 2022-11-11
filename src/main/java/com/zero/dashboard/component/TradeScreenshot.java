package com.zero.dashboard.component;

import cn.hutool.core.date.StopWatch;
import cn.hutool.system.OsInfo;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
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
//
//    //TODO 多线程改造，每个线程拥有一个Driver,每使用100次重新连接
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
//            options.setCapability("pageLoadStrategy", "none");
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
        log.info("driver:" + DRIVER);
        stopWatch.start("打开网页");
        DRIVER.get(url);
        stopWatch.stop();

        stopWatch.start("等待渲染完成");
        new WebDriverWait(DRIVER, 5, 3).until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));


        // 1.5-2秒
//        try {
//            Thread.sleep(30);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        stopWatch.stop();

        stopWatch.start("截图");
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(0)).takeScreenshot(DRIVER);
        stopWatch.stop();

        BufferedImage image = screenshot.getImage();

        log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return image;
    }
}
