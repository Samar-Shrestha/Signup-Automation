package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public abstract class PageBase {

    protected WebDriver driver;

    public PageBase(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected void click(By locator) {
        Waits.untilClickable(driver, locator).click();
    }

    protected void click(WebElement el) {
        Waits.untilClickable(driver, el).click();
    }

    protected void type(By locator, String text) {
        WebElement el = Waits.untilVisible(driver, locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(WebElement el, String text) {
        Waits.untilClickable(driver, el);
        el.clear();
        el.sendKeys(text);
    }

    protected void selectOption(WebElement el, String text) {
        scrollTo(el);
        try {
            new Select(el).selectByVisibleText(text);
        } catch (Exception e) {
            // try partial match
            new Select(el).getOptions().stream()
                .filter(o -> o.getText().toLowerCase().contains(text.toLowerCase()))
                .findFirst().ifPresent(o -> new Select(el).selectByVisibleText(o.getText()));
        }
    }

    protected void scrollTo(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", el);
        Waits.pause(300);
    }

    protected void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected String currentUrl() {
        return driver.getCurrentUrl();
    }
}