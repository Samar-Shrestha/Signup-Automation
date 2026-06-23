package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProfessionalPage extends PageBase {

    @FindBy(name = "number_of_students_recruited_annually")
    private WebElement numberOfStudentsField;

    @FindBy(name = "focus_area")
    private WebElement focusAreaField;

    @FindBy(name = "success_metrics")
    private WebElement successMetricsField;

    @FindBy(css = "button[type='submit']")
    private WebElement nextBtn;

    public ProfessionalPage(WebDriver driver) {
        super(driver);
    }

    public void fillForm() {
        Waits.pause(1500);

        selectYearsOfExperience("1 year");

        safeType(numberOfStudentsField, "50",                                    "Number of Students");
        safeType(focusAreaField,        "Undergraduate admissions to Australia", "Focus Area");
        safeType(successMetricsField,   "85",                                    "Success Metrics");

        checkByLabel("Career Counseling");
        checkByLabel("Admission Applications");
        checkByLabel("Visa Processing");
        checkByLabel("Test Prepration");
    }

    private void selectYearsOfExperience(String optionText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1 — wait for combobox to be clickable, then click
            WebElement trigger = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[role='combobox']")
            ));
            scrollTo(trigger);
            jsClick(trigger); // use JS click to avoid interception
            System.out.println("Clicked Years of Experience combobox");
            Waits.pause(1200);

            // Step 2 — after click, React re-renders portal
            // Re-find the span fresh — DO NOT reuse old reference
            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[normalize-space(text())='" + optionText + "']")
            ));
            jsClick(option); // JS click avoids stale/intercept issues
            Waits.pause(600);
            System.out.println("Selected Years of Experience: " + optionText);

        } catch (Exception e) {
            System.err.println("Could not select Years of Experience '" + optionText + "': " + e.getMessage());
        }
    }

    private void checkByLabel(String labelText) {
        try {
            WebElement checkbox = driver.findElement(By.xpath(
                "//label[normalize-space(text())='" + labelText + "']/" +
                "preceding-sibling::button[@role='checkbox']"
            ));
            scrollTo(checkbox);
            String checked = checkbox.getAttribute("aria-checked");
            if (!"true".equals(checked)) {
                click(checkbox);
                System.out.println("Checked: " + labelText);
            } else {
                System.out.println("Already checked: " + labelText);
            }
        } catch (Exception e) {
            System.err.println("Could not check '" + labelText + "': " + e.getMessage());
        }
    }

    private void safeType(WebElement el, String value, String label) {
        try {
            scrollTo(el);
            el.clear();
            el.sendKeys(value);
            System.out.println("Filled " + label + ": " + value);
        } catch (Exception e) {
            System.err.println("Could not fill " + label + ": " + e.getMessage());
        }
    }

    public void clickNext() {
        try {
            // Wait for submit button to be clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[type='submit']")
            ));
            scrollTo(btn);
            jsClick(btn);
        } catch (Exception e) {
            jsClick(nextBtn);
        }
        Waits.pause(2000);
        System.out.println("Professional form submitted. URL: " + currentUrl());
    }
}