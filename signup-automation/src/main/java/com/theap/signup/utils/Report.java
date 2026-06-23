package com.theap.signup.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();
    private static final String FOLDER = "test-output/reports";

    private Report() {}

    public static ExtentReports getInstance() {
        if (extent == null) extent = setup();
        return extent;
    }

    private static ExtentReports setup() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = FOLDER + "/SignupReport_" + timestamp + ".html";
        new File(FOLDER).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(path);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Signup Automation Report");
        spark.config().setReportName("THE Authorized Partner - Registration Flow");

        ExtentReports reports = new ExtentReports();
        reports.attachReporter(spark);
        reports.setSystemInfo("App", "https://authorized-partner.vercel.app");
        reports.setSystemInfo("Browser", Settings.get().browser());
        return reports;
    }

    public static ExtentTest currentTest()              { return currentTest.get(); }
    public static void setCurrentTest(ExtentTest test)  { currentTest.set(test); }
    public static void save()                           { if (extent != null) extent.flush(); }
}