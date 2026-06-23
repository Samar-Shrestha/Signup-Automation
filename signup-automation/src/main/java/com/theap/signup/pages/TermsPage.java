package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TermsPage extends PageBase {

    // Agree checkbox
    @FindBy(id = "remember")
    private WebElement agreeCheckbox;

    // Continue button
    @FindBy(xpath = "//button[normalize-space()='Continue']")
    private WebElement continueBtn;

    // Page load indicator
    private static final By AGREE_BTN = By.id("remember");

    public TermsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            Waits.untilVisible(driver, AGREE_BTN);
            System.out.println("Terms page loaded.");
            return true;
        } catch (Exception e) {
            System.out.println("Terms page NOT loaded: " + e.getMessage());
            return false;
        }
    }

    public void acceptTerms() {
        Waits.untilClickable(driver, agreeCheckbox);
        jsClick(agreeCheckbox);
        Waits.pause(500);
        System.out.println("Agree checkbox clicked.");
    }

    public void clickContinue() {
        Waits.untilClickable(driver, continueBtn);
        click(continueBtn);
        Waits.pause(1000);
        System.out.println("Continue clicked. URL: " + currentUrl());
    }

    public void proceed() {
        acceptTerms();
        clickContinue();
    }
}