package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends PageBase {

    // Get Started button
    @FindBy(xpath = "//button[normalize-space()='Get Started']")
    private WebElement getStartedBtn;

    private static final By GET_STARTED = By.xpath("//button[normalize-space()='Get Started']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickGetStarted() {
        Waits.untilClickable(driver, GET_STARTED);
        click(getStartedBtn);
        Waits.pause(1000);
        System.out.println("Get Started clicked. URL: " + currentUrl());
    }
}