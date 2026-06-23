package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AgencyPage extends PageBase {

    @FindBy(name = "agency_name")
    private WebElement agencyNameField;

    @FindBy(name = "role_in_agency")
    private WebElement roleInAgencyField;

    @FindBy(name = "agency_email")
    private WebElement agencyEmailField;

    @FindBy(name = "agency_website")
    private WebElement agencyWebsiteField;

    @FindBy(name = "agency_address")
    private WebElement agencyAddressField;

    @FindBy(css = "button[role='combobox'][aria-haspopup='dialog']")
    private WebElement regionCombobox;

    @FindBy(css = "button[type='submit']")
    private WebElement nextBtn;

    public AgencyPage(WebDriver driver) {
        super(driver);
    }

    public void fillForm(String name, String role, String email,
                         String website, String address, String region) {
        Waits.pause(1500);

        safeType(agencyNameField,    name,    "Agency Name");
        safeType(roleInAgencyField,  role,    "Role in Agency");
        safeType(agencyEmailField,   email,   "Agency Email");
        safeType(agencyWebsiteField, website, "Agency Website");
        safeType(agencyAddressField, address, "Agency Address");

        selectRegion(region);
    }

    private void selectRegion(String regionText) {
        try {
            scrollTo(regionCombobox);
            click(regionCombobox);
            Waits.pause(1200);

            // The option is a plain <span> with exact text — no role="option"
            WebElement option = driver.findElement(By.xpath(
                "//span[normalize-space(text())='" + regionText + "']"
            ));
            scrollTo(option);
            click(option);
            Waits.pause(500);
            System.out.println("Selected Region: " + regionText);

        } catch (Exception e) {
            System.err.println("Could not select Region '" + regionText + "': " + e.getMessage());
        }
    }

    public void clickNext() {
        try {
            scrollTo(nextBtn);
            click(nextBtn);
        } catch (Exception e) {
            jsClick(nextBtn);
        }
        Waits.pause(1500);
        System.out.println("Agency form submitted. URL: " + currentUrl());
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
}