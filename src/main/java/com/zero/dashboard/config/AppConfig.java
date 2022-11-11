package com.zero.dashboard.config;

import cn.hutool.system.OsInfo;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.*;

@Slf4j
@Configuration
public class AppConfig {


    @Bean(name = "drivers")
    public ThreadPoolExecutor pool(){
        int factor = 2;
        int availableProcessors = Runtime.getRuntime().availableProcessors() * factor;
        log.info("浏览器驱动线程数[{}]", availableProcessors);
        return new ThreadPoolExecutor(
                availableProcessors, availableProcessors,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue(),Executors.defaultThreadFactory()
        );
    }



    @Bean(name = "chromes")
    public LinkedBlockingQueue<WebDriver> chromes(){
        LinkedBlockingQueue chromes = new LinkedBlockingQueue();
        for (int i = 0; i < 4; i++) {
            ChromeOptions options = new ChromeOptions();
            if(new OsInfo().isLinux()){
                options.setBinary("/opt/google/chrome/chrome");
                options.addArguments(
                        "--headless",
                        "--no-sandbox",
                        "--window-size=1920,1080" ,
                        "--disable-dev-shm-usage",
                        "--disk-cache-dir=/opt/selenium/opt/dashboard/cache");
                options.setCapability("pageLoadStrategy", "none");
            }
            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
            Actions actions = new Actions(driver);
            actions.moveByOffset(920, 0).perform();

            chromes.add(driver);
        }
        return chromes;
    }

}
