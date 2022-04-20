package com.zero.dashborad;

import cn.hutool.system.OsInfo;
import com.zero.dashborad.component.LongPictureScreenshot;
import com.zero.dashborad.component.TouTiaoScreenshot;
import com.zero.dashborad.component.Web2Pdf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class DashboradApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboradApplication.class, args);
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

}
