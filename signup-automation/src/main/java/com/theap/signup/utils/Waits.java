package com.theap.signup.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waits {

    private static final int TIMEOUT = Settings.get().explicitWait();

    private Waits() {}

    private static WebDriverWait wait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
    }

    public static WebElement untilVisible(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement untilClickable(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement untilClickable(WebDriver driver, WebElement element) {
        return wait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean untilUrlHas(WebDriver driver, String text) {
        return wait(driver).until(ExpectedConditions.urlContains(text));
    }

    public static WebElement untilPresent(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void pause(int milliseconds) {
        try { Thread.sleep(milliseconds); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}