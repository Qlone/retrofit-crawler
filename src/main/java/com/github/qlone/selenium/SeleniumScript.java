package com.github.qlone.selenium;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author heweinan
 * @date 2020-12-02 15:34
 */
public class SeleniumScript {
    private String javascript;
    private Collection<?> argument;
    private int blockTime;
    private TimeUnit blockTimUnit;

    public SeleniumScript(String javascript, Collection<?> argument,int blockTime,TimeUnit blockTimUnit) {
        this.javascript = javascript;
        this.argument = argument;
        this.blockTime = blockTime;
        this.blockTimUnit = blockTimUnit;
    }

    public String getJavascript() {
        return javascript;
    }

    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }

    public Collection<?> getArgument() {
        return argument;
    }

    public void setArgument(Collection<?> argument) {
        this.argument = argument;
    }

    public int getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(int blockTime) {
        this.blockTime = blockTime;
    }

    public TimeUnit getBlockTimUnit() {
        return blockTimUnit;
    }

    public void setBlockTimUnit(TimeUnit blockTimUnit) {
        this.blockTimUnit = blockTimUnit;
    }
}
