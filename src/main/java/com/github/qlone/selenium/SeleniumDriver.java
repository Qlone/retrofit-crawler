package com.github.qlone.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public enum SeleniumDriver{
    Chrome(){
        @Override
        WebDriver driver(String path) {
            System.setProperty("webdriver.chrome.driver",path);
            return new ChromeDriver();
        }},
    Phantomjs(){
        @Override
        WebDriver driver(String path) {
            System.setProperty("phantomjs.binary.path",path);
            return new PhantomJSDriver();
        }
    };

    abstract WebDriver driver(String path);


}