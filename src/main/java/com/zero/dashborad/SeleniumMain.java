package com.zero.dashborad;

import cn.hutool.http.useragent.OS;
import cn.hutool.system.OsInfo;
import com.zero.dashborad.component.LongPictureScreenshot;
import com.zero.dashborad.component.TouTiaoScreenshot;
import com.zero.dashborad.component.Web2Pdf;
import com.zero.dashborad.strategy.FixedHeaderViewportPastingDecorator;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SeleniumMain {

//    public static void main(String[] args) {
//        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\chromedriver_win32\\chromedriver.exe");
////        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\99.0.4844.51_chrome64_stable_windows_installer.exe");
//        ChromeDriver driver = new ChromeDriver();
//        driver.get("https://www.toutiao.com/article/7087963907754590757");
//        File file = new CustomScreenshot().fullScreenshotLong(driver);
//        System.out.println();
//    }


    public static void main(String[] args) {
        if(new OsInfo().isWindows()){
            System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\chromedriver_win32\\chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", "/opt/chromedriver.exe");
        }
//        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\chromedriver_98.0.4758.102.exe");
        new TouTiaoScreenshot().exec("https://www.toutiao.com/article/7087963907754590757", "./" + "AShot_BBC_Entire_" + new Date().getTime() + ".png");
        new LongPictureScreenshot().exec("https://www.qq.com", "./" + "AShot_BBC_Entire_" + new Date().getTime() + ".png");
        new Web2Pdf().exec("https://www.qq.com", "./" + "AShot_BBC_Entire_" + new Date().getTime() + ".pdf");
    }

    public static void caa(String[] args) {
        // TODO Auto-generated method stub
//        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\chromedriver_win32\\chromedriver.exe");
//        toPdf();
        ChromeOptions options = new ChromeOptions();
        // 设置谷歌浏览器exe文件所在地址
//        options.setBinary("C:\\Users\\qizhan\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
        // 这里是要执行的命令，如需修改截图页面的尺寸，修改--window-size的参数即可
//        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);

//        WebDriver driver = new ChromeDriver();
        try {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get("https://www.toutiao.com/article/7087963907754590757");
//            driver.get("https://www.cjavapy.com/article/613/");
//

            // 单页面截图
            //等待页面加载完成
            new WebDriverWait(driver, 300000).until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete"));
            JavascriptExecutor jexec = (JavascriptExecutor) driver;
            Thread.sleep(5 * 1000);

//            int width = ((Long)jexec.executeScript("return document.body.scrollWidth")).intValue();
//            int height = ((Long)jexec.executeScript("return document.body.scrollHeight")).intValue();
////            //设置浏览窗口大小
//            driver.manage().window().setSize(new Dimension(width, height));
//            Screenshot screenshot = new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver);





            // 滚动长截图
          //take screenshot of the entire page
//            Screenshot screenshot = new AShot().addIgnoredArea(new Coords(0,0,1920, 60)).shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
//            Screenshot screenshot=new AShot().shootingStrategy(ShootingStrategies.viewportNonRetina(1000, new FixedCutStrategy(60, 0))).takeScreenshot(driver);

//            SimpleShootingStrategy simpleShootingStrategy = new SimpleShootingStrategy();
//            Set<Coords> shootingAreas = new HashSet<>();
//            shootingAreas.add(new Coords(0, 60, 1920, 1200));
//            simpleShootingStrategy.prepareCoords(shootingAreas);
            FixedHeaderViewportPastingDecorator shootingStrategy = (new FixedHeaderViewportPastingDecorator(new SimpleShootingStrategy(), 64)).withScrollTimeout(100);
//            ShootingStrategy shootingStrategy = ShootingStrategies.viewportPasting(100);
//            shootingStrategy.prepareCoords(shootingAreas);
            AShot aShot = new AShot()
//                    .addIgnoredArea(new Coords(0, 60, 1920, 1200))
                    .shootingStrategy(shootingStrategy);
            Screenshot screenshot = aShot
                    .takeScreenshot(driver);

            BufferedImage image = screenshot.getImage();
            ImageIO.write(image, "PNG", new File("D:\\temp\\" + "AShot_BBC_Entire_" + new Date().getTime() + ".png"));
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        driver.quit();
    }

    public static void a(String[] args) {
        toPdf();
    }

    private static void toPdf(){
        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\chromedriver_98.0.4758.102.exe");
//        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\chromedriver_win32\\chromedriver.exe");
        try
        {
            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless", "--disable-gpu", "--run-all-compositor-stages-before-draw");
            options.addArguments("--headless");
//            options.setBinary("D:\\develop\\chrome-driver\\98.0.4758.102_chrome64_stable_windows_installer.exe");
            ChromeDriver chromeDriver = new ChromeDriver(options);
//            chromeDriver.get("https://www.toutiao.com/article/7087963907754590757");
            chromeDriver.get("https://www.qq.com/");
            Map<String, Object> params = new HashMap();
//            chromeDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//            chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            chromeDriver.manage().window().maximize();
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String command = "Page.printToPDF";
            Map<String, Object> output = chromeDriver.executeCdpCommand(command, params);

            try
            {
                FileOutputStream fileOutputStream = new FileOutputStream("D://a.pdf");
                byte[] byteArray = java.util.Base64.getDecoder().decode((String) output.get("data"));
                fileOutputStream.write(byteArray);
                fileOutputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            throw e;
        }
    }
}
