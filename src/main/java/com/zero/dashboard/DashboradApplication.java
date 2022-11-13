package com.zero.dashboard;

import cn.hutool.system.OsInfo;
import com.zero.dashboard.component.LongPictureScreenshot;
import com.zero.dashboard.component.TouTiaoScreenshot;
import com.zero.dashboard.component.Web2Pdf;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@Slf4j
@MapperScan("com.zero.dashboard.mapper")
@SpringBootApplication
public class DashboradApplication {

    public static void main(String[] args) {
        String fileHomePath = "./";
        if(new OsInfo().isWindows()){
            System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome-driver\\107.0.5304.62\\chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", "/opt/selenium/chromedriver-98.0.4758.80");
            fileHomePath = "/opt/selenium/opt/dashboard/report/";
        }
        SpringApplication.run(DashboradApplication.class, args);

//                log.info("头条截图");
//        try {
//            new TouTiaoScreenshot().exec("https://www.toutiao.com/article/7087963907754590757", fileHomePath + "TouTiao_" + new Date().getTime() + ".png");
//        } catch (Exception e) {
//            log.error("", e);
//        }

    }

}
