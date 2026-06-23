package com.theap.signup.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class MailinatorHelper {

    private static final String MAILINATOR_URL =
        "https://www.mailinator.com/v4/public/inboxes.jsp?to=";

    private MailinatorHelper() {}

    public static String readOtp(WebDriver driver, String emailPrefix) {
        String otp = "";
        String originalTab = driver.getWindowHandle();

        try {
            // Step 1 - Open Mailinator in a NEW tab
            ((JavascriptExecutor) driver).executeScript("window.open()");
            Waits.pause(1000);

            // Step 2 - Switch to new tab
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));
            System.out.println("Switched to new tab.");

            // Step 3 - Go to Mailinator inbox
            driver.get(MAILINATOR_URL + emailPrefix);
            System.out.println("Opened Mailinator for: " + emailPrefix);

            // Step 4 - Wait for email to arrive (max 30 seconds)
            otp = waitForOtp(driver, 30);

        } catch (Exception e) {
            System.err.println("Mailinator error: " + e.getMessage());
        } finally {
            // Step 5 - Close Mailinator tab
            try {
                driver.close();
                System.out.println("Closed Mailinator tab.");
            } catch (Exception e) {
                System.err.println("Could not close tab: " + e.getMessage());
            }

            // Step 6 - Switch back to original registration tab
            driver.switchTo().window(originalTab);
            System.out.println("Switched back to registration tab.");
            Waits.pause(1000);
        }

        return otp;
    }

    /**
     * Keeps refreshing inbox until email arrives or timeout reached.
     */
    private static String waitForOtp(WebDriver driver, int maxWaitSeconds) {
        String otp = "";
        int waited = 0;

        while (waited < maxWaitSeconds) {
            try {
                // Refresh inbox
                driver.navigate().refresh();
                Waits.pause(3000);
                waited += 3;

                // Look for email from theauthorizedpartner
                By emailRow = By.xpath(
                    "//td[contains(@onclick,'showTheMessage')]");

                List<WebElement> emails = driver.findElements(emailRow);

                if (!emails.isEmpty()) {
                    System.out.println("Email found! Clicking...");
                    emails.get(0).click();
                    Waits.pause(3000);

                    // Switch into iframe if present
                    List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                    if (!iframes.isEmpty()) {
                        driver.switchTo().frame(iframes.get(0));
                        System.out.println("Switched into email iframe.");
                    }

                    // Find OTP - bold centered text
                    By otpLocator = By.xpath(
                        "//p[contains(@style,'font-weight: bold') " +
                        "and contains(@style,'letter-spacing')]");

                    List<WebElement> otpEl = driver.findElements(otpLocator);

                    if (!otpEl.isEmpty()) {
                        otp = otpEl.get(0).getText().trim();
                        System.out.println("OTP found: " + otp);
                    }

                    // Fallback - scan full body text
                    if (otp.isEmpty()) {
                        String bodyText = driver
                            .findElement(By.tagName("body")).getText();
                        otp = extractSixDigits(bodyText);
                        System.out.println("OTP from body scan: " + otp);
                    }

                    driver.switchTo().defaultContent();

                    if (!otp.isEmpty()) break;
                }

                System.out.println("Waiting for email... " + waited + "s");

            } catch (Exception e) {
                System.err.println("Inbox check error: " + e.getMessage());
                driver.switchTo().defaultContent();
            }
        }

        if (otp.isEmpty()) {
            System.err.println("OTP not found after " + maxWaitSeconds + " seconds!");
        }

        return otp;
    }

    private static String extractSixDigits(String text) {
        for (String word : text.split("\\s+")) {
            String cleaned = word.replaceAll("[^0-9]", "");
            if (cleaned.length() == 6) {
                return cleaned;
            }
        }
        return "";
    }
}