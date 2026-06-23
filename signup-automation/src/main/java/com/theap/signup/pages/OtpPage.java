package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class OtpPage extends PageBase {

    // All 6 OTP input boxes
    private static final By OTP_INPUTS = By.xpath(
        "//input[@data-input-otp='true'] | //input[@inputmode='numeric'][@maxlength='1'] | //input[@maxlength='6']"
    );

    // Verify Code button
    @FindBy(xpath = "//button[normalize-space()='Verify Code']")
    private WebElement verifyBtn;

    public OtpPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            Waits.untilVisible(driver, OTP_INPUTS);
            System.out.println("OTP page loaded.");
            return true;
        } catch (Exception e) {
            System.out.println("OTP page not detected.");
            return false;
        }
    }

    /**
     * Types OTP code into the input field.
     * Handles both single input (maxlength=6) and 6 separate boxes.
     */
    public void enterOtp(String otpCode) {
        try {
            List<WebElement> inputs = driver.findElements(OTP_INPUTS);

            if (inputs.size() == 1) {
                // Single input field accepting all 6 digits
                WebElement input = inputs.get(0);
                scrollTo(input);
                input.clear();
                input.sendKeys(otpCode);
                System.out.println("OTP entered in single field: " + otpCode);

            } else if (inputs.size() >= 6) {
                // 6 separate boxes - type one digit each
                for (int i = 0; i < 6; i++) {
                    inputs.get(i).sendKeys(String.valueOf(otpCode.charAt(i)));
                    Waits.pause(100);
                }
                System.out.println("OTP entered in 6 boxes: " + otpCode);

            } else {
                System.err.println("OTP input not found!");
            }

        } catch (Exception e) {
            System.err.println("OTP entry failed: " + e.getMessage());
        }
    }

    public void clickVerify() {
        try {
            scrollTo(verifyBtn);
            click(verifyBtn);
        } catch (Exception e) {
            jsClick(verifyBtn);
        }
        Waits.pause(2000);
        System.out.println("Verify clicked. URL: " + currentUrl());
    }

    public void verifyOtp(String otpCode) {
        enterOtp(otpCode);
        clickVerify();
    }
}