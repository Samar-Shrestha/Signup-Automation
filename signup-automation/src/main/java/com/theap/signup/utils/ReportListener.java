package com.theap.signup.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ReportListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        Report.getInstance();
        System.out.println("===== Suite Started: " + context.getName() + " =====");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReports extent = Report.getInstance();
        ExtentTest test = extent.createTest(
            result.getMethod().getMethodName(),
            result.getMethod().getDescription()
        );
        Report.setCurrentTest(test);
        System.out.println("--> Running: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = Report.currentTest();
        if (test != null) {
            test.log(Status.PASS, "PASSED: " + result.getName());
        }
        System.out.println("PASSED: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = Report.currentTest();
        if (test != null) {
            test.log(Status.FAIL, "FAILED: " + result.getThrowable().getMessage());
        }
        System.out.println("FAILED: " + result.getName());

        try {
            String path = Screenshot.take(BrowserDriver.open(), "FAIL_" + result.getName());
            if (!path.isEmpty() && test != null) {
                test.addScreenCaptureFromPath(path, "Failure Screenshot");
            }
        } catch (Exception e) {
            System.err.println("Could not take screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = Report.currentTest();
        if (test != null) {
            test.log(Status.SKIP, "SKIPPED: " + result.getName());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        Report.save();
        System.out.println("===== Suite Finished =====");
    }
}