package com.theap.signup.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class BrowserDriver {

    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();
    private static final Settings settings = Settings.get();

    private BrowserDriver() {}

    public static WebDriver open() {
        if (driverHolder.get() == null) {
            driverHolder.set(launchBrowser());
        }
        return driverHolder.get();
    }

    private static WebDriver launchBrowser() {
        String browser = settings.browser().toLowerCase();
        boolean headless = settings.headless();
        WebDriver driver;

        if (browser.equals("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (headless) options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            if (headless) options.addArguments("--headless=new");
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            driver = new ChromeDriver(options);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(settings.implicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(settings.pageLoadTimeout()));
        driver.manage().window().maximize();
        return driver;
    }

    public static void close() {
        if (driverHolder.get() != null) {
            // ── Wait 5 seconds so success page is visible before closing ─
            try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            driverHolder.get().quit();
            driverHolder.remove();
        }
    }
}