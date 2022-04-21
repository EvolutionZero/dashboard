package com.zero.dashborad;

import cn.hutool.system.OsInfo;
import com.zero.dashborad.component.LongPictureScreenshot;
import com.zero.dashborad.component.TouTiaoScreenshot;
import com.zero.dashborad.component.Web2Pdf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@Slf4j
@SpringBootApplication
public class DashboradApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboradApplication.class, args);
        String fileHomePath = "./";
        if(new OsInfo().isWindows()){
            System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\chromedriver_win32\\chromedriver.exe");
        } else {
//            System.setProperty("webdriver.chrome.driver", "/opt/chromedriver");
            System.setProperty("webdriver.chrome.driver", "/opt/selenium/chromedriver-98.0.4758.80");
            fileHomePath = "/opt/selenium/";
        }
        log.info("头条截图");
        new TouTiaoScreenshot().exec("https://www.toutiao.com/article/7087963907754590757", fileHomePath + "TouTiao_" + new Date().getTime() + ".png");
        log.info("QQ截图");
        new LongPictureScreenshot().exec("https://www.qq.com", fileHomePath + "qq_" + new Date().getTime() + ".png");
        log.info("转PDF");
        new Web2Pdf().exec("https://www.qq.com", fileHomePath + "qq_" + new Date().getTime() + ".pdf");
        log.info("完成");
    }

}
