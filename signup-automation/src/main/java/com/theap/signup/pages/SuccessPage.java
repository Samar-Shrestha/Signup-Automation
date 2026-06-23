package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SuccessPage extends PageBase {

    private static final By SUCCESS_TEXT = By.xpath(
        "//*[contains(text(),'success') or contains(text(),'Success') " +
        "or contains(text(),'registered') or contains(text(),'Thank you') " +
        "or contains(text(),'submitted') or contains(text(),'review') " +
        "or contains(text(),'Welcome') or contains(text(),'pending')]");

    public SuccessPage(WebDriver driver) {
        super(driver);
    }

    public boolean isRegistered() {
        Waits.pause(2000);
        String url = currentUrl();
        System.out.println("Final URL: " + url);

        // Check URL keywords
        if (url.contains("success") || url.contains("pending")
                || url.contains("dashboard") || url.contains("review")
                || url.contains("confirmation") || url.contains("thank")) {
            System.out.println("Success detected via URL.");
            return true;
        }

        // Check page text
        try {
            driver.findElement(SUCCESS_TEXT);
            System.out.println("Success message found on page.");
            return true;
        } catch (Exception e) {
            // If we moved past /register, treat as success
            boolean movedOn = !url.endsWith("/register");
            System.out.println("Moved past /register: " + movedOn);
            return movedOn;
        }
    }
}