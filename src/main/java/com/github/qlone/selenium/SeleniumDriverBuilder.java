package com.github.qlone.selenium;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author heweinan
 * @date 2020-12-02 10:34
 */
public class SeleniumDriverBuilder {

    private SeleniumDriver driverContent;
    private String path;
    private static final ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();
    private static List<WebDriver> webDrivers = new ArrayList<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            for(WebDriver webDriver: webDrivers){
                webDriver.close();
                webDriver.quit();
                return;
            }
        }));
    }

    public WebDriver get(){
        if (driverContent == null) {
            throw new IllegalStateException("WebDriver required.");
        }
        WebDriver webDriver = webDriverThreadLocal.get();
        if (webDriver == null) {
            webDriver = driverContent.driver(path);
            webDriver.manage().window().maximize();
            webDriverThreadLocal.set(webDriver);
            addWebDriver(webDriver);
        }
        return webDriver;
    }

    synchronized private void addWebDriver(WebDriver webDriver){
        webDrivers.add(webDriver);
    }

    public SeleniumDriverBuilder driver(SeleniumDriver driverContent){
        Objects.requireNonNull(driverContent);
        this.driverContent = driverContent;
        return this;
    }

    public SeleniumDriverBuilder driverPath(String path){
        Objects.requireNonNull(path);
        this.path = path;
        return this;
    }

    public SeleniumDriver getDriverContent() {
        return driverContent;
    }
}
