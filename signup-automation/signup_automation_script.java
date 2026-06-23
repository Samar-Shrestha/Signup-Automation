package com.theap.signup.tests;

import com.theap.signup.pages.*;
import com.theap.signup.utils.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class SignupTest {

    private WebDriver driver;
    private Settings settings = Settings.get();

    // Random email generated once per run
    private String emailPrefix = FakeData.emailPrefix();            
    private String email       = FakeData.randomEmail(emailPrefix); 

    // Everything else is FakeData (random each run) 
    private String firstName = FakeData.firstName();
    private String lastName  = FakeData.lastName();
    private String phone     = FakeData.phone();
    private String password  = FakeData.password();

    @BeforeClass
    public void setUp() {
        driver = BrowserDriver.open();
        driver.get(settings.baseUrl());
        System.out.println("Opened: " + settings.baseUrl());
        System.out.println("Using email: " + email);
    }

    @Test(priority = 1, description = "Click Get Started on homepage")
    public void clickGetStarted() {
        HomePage home = new HomePage(driver);
        home.clickGetStarted();
        System.out.println("URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 2, dependsOnMethods = "clickGetStarted",
          description = "Accept Terms and Conditions")
    public void acceptTermsAndContinue() {
        TermsPage terms = new TermsPage(driver);
        Assert.assertTrue(terms.isLoaded(), "Terms page must be visible");
        terms.proceed();
        System.out.println("Terms done. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 3, dependsOnMethods = "acceptTermsAndContinue",
          description = "Fill Step 1 - Account Setup")
    public void fillAccountSetup() {
        AccountSetupPage account = new AccountSetupPage(driver);
        account.fillForm(firstName, lastName, email, phone, password);
        account.clickNext();
        System.out.println("Account setup done. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 4, dependsOnMethods = "fillAccountSetup",
          description = "Read OTP from Mailinator and verify email")
    public void verifyEmailOtp() {
        System.out.println("Opening Mailinator for: " + emailPrefix);
        String otp = MailinatorHelper.readOtp(driver, emailPrefix);
        System.out.println("OTP received: " + otp);

        Assert.assertFalse(otp.isEmpty(), "OTP must be found in Mailinator inbox");

        OtpPage otpPage = new OtpPage(driver);
        otpPage.verifyOtp(otp);
        System.out.println("OTP verified. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 5, dependsOnMethods = "verifyEmailOtp",
          description = "Fill Step 2 - Agency Details")
    public void fillAgencyDetails() {
        AgencyPage agency = new AgencyPage(driver);
        agency.fillForm(
            FakeData.agencyName(),   // Name
            "Director",              // Role in Agency
            FakeData.agencyEmail(),  // Email Address
            FakeData.website(),      // Website
            FakeData.address(),      // Address
            "Nepal"                  // Region of Operation
        );
        agency.clickNext();
        System.out.println("Agency done. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 6, dependsOnMethods = "fillAgencyDetails",
          description = "Fill Step 3 - Professional Experience")
    public void fillProfessionalExperience() {
        ProfessionalPage professional = new ProfessionalPage(driver);
        professional.fillForm();
        professional.clickNext();
        System.out.println("Professional done. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 7, dependsOnMethods = "fillProfessionalExperience",
          description = "Fill Step 4 - Verification and Preferences")
    public void fillVerificationAndPreferences() {
        VerificationPage verification = new VerificationPage(driver);
        verification.fillForm();
        verification.clickSubmit();
        System.out.println("Verification done. URL: " + driver.getCurrentUrl());
    }

    @Test(priority = 8, dependsOnMethods = "fillVerificationAndPreferences",
          description = "Verify signup completed successfully")
    public void verifySuccess() {
        SuccessPage success = new SuccessPage(driver);
        boolean registered = success.isRegistered();
        Screenshot.take(driver, "final_state");
        Assert.assertTrue(registered, "Registration should complete!");
        System.out.println("SIGNUP COMPLETE! Email used: " + email);
    }

    @AfterClass
    public void tearDown() {
        Report.save();
        BrowserDriver.close();
    }
}