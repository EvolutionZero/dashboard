package com.zero.dashborad;

import org.openqa.selenium.*;

import java.io.File;

public class CustomScreenshot {

    public File fullScreenshotLong(WebDriver driver) {

// 调整窗口

        resetWindowSizeToScreenshot(driver);

// 截图

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

// 调整窗口

        resetWindowSize(driver);

        return file;

    }

    private void resetWindowSize(WebDriver driver) {

        driver.manage().window().maximize();

    }

    /**
     * 上次高度

     */

    /**
     * 向下滑动次数
     */

    private int scrollTimes = 10;

    private int lastHeight = 0;

    /**
     * 重置窗口大小(调整至可以正常截图)
     *
     * @param driver 驱动对象
     */

    private void resetWindowSizeToScreenshot(WebDriver driver) {

// 窗口最大化

        resetWindowSize(driver);

// 向下滑动页面：到指定次数 || 高度不再变化，退出

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        for (int i = 0; i < scrollTimes; i++) {

// 获取当前高度

            Object thisHeightObject = javascriptExecutor.executeScript("return document.body.scrollHeight;");

            int thisHeight = Integer.parseInt(String.valueOf(thisHeightObject));

// 判断高度

            if (lastHeight != thisHeight) {

// 向下滑动

                javascriptExecutor.executeScript("window.scrollBy(0,10000)");

// 滑动后赋值

                lastHeight = thisHeight;

            } else {

// 高度相同，跳出

                break;

            }

        }

// 设置窗口高度

        Dimension size = driver.manage().window().getSize();

        driver.manage().window().setSize(new Dimension(size.width, lastHeight));

    }

}
