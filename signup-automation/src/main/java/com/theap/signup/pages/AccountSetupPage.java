package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountSetupPage extends PageBase {

    @FindBy(name = "firstName")
    private WebElement firstNameField;

    @FindBy(name = "lastName")
    private WebElement lastNameField;

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(name = "phoneNumber")
    private WebElement phoneField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(name = "confirmPassword")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//button[normalize-space()='Next']")
    private WebElement nextBtn;

    private static final By FIRST_NAME = By.name("firstName");

    public AccountSetupPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            Waits.untilVisible(driver, FIRST_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void fillForm(String firstName, String lastName,
                         String email, String phone, String password) {

        safeType(firstNameField,       firstName, "First Name");
        safeType(lastNameField,        lastName,  "Last Name");
        safeType(emailField,           email,     "Email");
        safeType(phoneField,           phone,     "Phone");
        safeType(passwordField,        password,  "Password");
        safeType(confirmPasswordField, password,  "Confirm Password");
    }

    public void clickNext() {
        try {
            scrollTo(nextBtn);
            click(nextBtn);
        } catch (Exception e) {
            jsClick(nextBtn);
        }
        Waits.pause(1500);
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