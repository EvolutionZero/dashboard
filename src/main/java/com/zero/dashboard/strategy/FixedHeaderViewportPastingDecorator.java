package com.zero.dashboard.strategy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
import ru.yandex.qatools.ashot.util.InnerScript;

public class FixedHeaderViewportPastingDecorator implements ShootingStrategy {
    protected int scrollTimeout = 0;
    private Coords shootingArea;

    private int headerHeightPx;


    private ShootingStrategy shootingStrategy;


    public ShootingStrategy getShootingStrategy() {
        return this.shootingStrategy;
    }


    public FixedHeaderViewportPastingDecorator(ShootingStrategy strategy, int headerHeightPx) {
        this.shootingStrategy = strategy;
        this.headerHeightPx = headerHeightPx;
    }

    public FixedHeaderViewportPastingDecorator withScrollTimeout(int scrollTimeout) {
        this.scrollTimeout = scrollTimeout;
        return this;
    }

    public BufferedImage getScreenshot(WebDriver wd) {
        return this.getScreenshot(wd, (Set)null);
    }

    public BufferedImage getScreenshot(WebDriver wd, Set<Coords> coordsSet) {
        JavascriptExecutor js = (JavascriptExecutor)wd;
        int pageHeight = this.getFullHeight(wd);
        int pageWidth = this.getFullWidth(wd);
        int viewportHeight = this.getWindowHeight(wd);
        this.shootingArea = this.getShootingCoords(coordsSet, pageWidth, pageHeight, viewportHeight);
        BufferedImage finalImage = new BufferedImage(pageWidth, this.shootingArea.height, 5);
        Graphics2D graphics = finalImage.createGraphics();

        boolean first = true;

        int curY = 0;
        while(true) {
            this.scrollVertically(js, curY);
            this.waitForScrolling();
            BufferedImage part = this.getShootingStrategy().getScreenshot(wd);
            graphics.drawImage(
                    part,
                    0, curY + (first ? 0 : headerHeightPx),
                    pageWidth, curY + (pageHeight) ,
                    0, first ? 0 : headerHeightPx,
                    pageWidth, pageHeight,
                    (ImageObserver)null);

            if(first){
                curY = this.shootingArea.y + viewportHeight  - headerHeightPx;

            } else {
                curY = curY + viewportHeight - headerHeightPx;
            }
            if(curY  >= this.shootingArea.getHeight()){
                break;
            }
            first = false;
        }
        graphics.dispose();
        return finalImage;
    }

    public Set<Coords> prepareCoords(Set<Coords> coordsSet) {
        return this.shootingArea == null ? coordsSet : this.shiftCoords(coordsSet, this.shootingArea);
    }

    public int getFullHeight(WebDriver driver) {
        return ((Number)InnerScript.execute("js/page_height.js", driver, new Object[0])).intValue();
    }

    public int getFullWidth(WebDriver driver) {
        return ((Number)InnerScript.execute("js/viewport_width.js", driver, new Object[0])).intValue();
    }

    public int getWindowHeight(WebDriver driver) {
        return ((Number)InnerScript.execute("js/viewport_height.js", driver, new Object[0])).intValue();
    }

    protected int getCurrentScrollY(JavascriptExecutor js) {
        return ((Number)js.executeScript("var scrY = window.scrollY;if(scrY){return scrY;} else {return 0;}", new Object[0])).intValue();
    }

    protected void scrollVertically(JavascriptExecutor js, int scrollY) {
        js.executeScript("scrollTo(0, arguments[0]); return [];", new Object[]{scrollY});
    }

    private Coords getShootingCoords(Set<Coords> coords, int pageWidth, int pageHeight, int viewPortHeight) {
        return coords != null && !coords.isEmpty() ? this.extendShootingArea(Coords.unity(coords), viewPortHeight, pageHeight) : new Coords(0, 0, pageWidth, pageHeight);
    }

    private Set<Coords> shiftCoords(Set<Coords> coordsSet, Coords shootingArea) {
        Set<Coords> shiftedCoords = new HashSet();
        if (coordsSet != null) {
            Iterator var4 = coordsSet.iterator();

            while(var4.hasNext()) {
                Coords coords = (Coords)var4.next();
                coords.y -= shootingArea.y;
                shiftedCoords.add(coords);
            }
        }

        return shiftedCoords;
    }

    private Coords extendShootingArea(Coords shootingCoords, int viewportHeight, int pageHeight) {
        int halfViewport = viewportHeight / 2;
        shootingCoords.y = Math.max(shootingCoords.y - halfViewport / 2, 0);
        shootingCoords.height = Math.min(shootingCoords.height + halfViewport, pageHeight);
        return shootingCoords;
    }

    private void waitForScrolling() {
        try {
            Thread.sleep((long)this.scrollTimeout);
        } catch (InterruptedException var2) {
            throw new IllegalStateException("Exception while waiting for scrolling", var2);
        }
    }
}
