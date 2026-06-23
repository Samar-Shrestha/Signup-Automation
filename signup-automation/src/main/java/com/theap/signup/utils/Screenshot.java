package com.theap.signup.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot {

    private static final String FOLDER = "test-output/screenshots";

    private Screenshot() {}

    public static String take(WebDriver driver, String label) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = FOLDER + "/" + label + "_" + timestamp + ".png";

        try {
            Files.createDirectories(Paths.get(FOLDER));
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), new File(filePath).toPath());
            System.out.println("Screenshot saved: " + filePath);
            return new File(filePath).getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
            return "";
        }
    }
}