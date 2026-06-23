package com.theap.signup.pages;

import com.theap.signup.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class VerificationPage extends PageBase {

    @FindBy(name = "business_registration_number")
    private WebElement registrationNumberField;

    @FindBy(css = "button[role='combobox'][aria-haspopup='dialog']")
    private WebElement preferredCountriesCombobox;

    @FindBy(name = "certification_details")
    private WebElement certificationDetailsField;

    @FindBy(css = "button[type='submit']")
    private WebElement submitBtn;

    public VerificationPage(WebDriver driver) {
        super(driver);
    }

    public void fillForm() {
        Waits.pause(1500);

        safeType(registrationNumberField, "REG-123456", "Business Registration Number");

        selectPreferredCountry("Australia");

        checkByLabel("Universities");
        checkByLabel("Colleges");
        checkByLabel("Vocational School");
        checkByLabel("Other");

        safeType(certificationDetailsField, "ICEF Certified Education Agent", "Certification Details");

        uploadDocuments();
    }

    // ── Create real minimal PDF in temp dir ───────────────────────────────
    private String createDummyPdf(String fileName) throws Exception {
        String tempPath = System.getProperty("java.io.tmpdir");
        File file = new File(tempPath + File.separator + fileName);

        // Minimal valid PDF structure
        String pdf =
            "%PDF-1.4\n" +
            "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n" +
            "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n" +
            "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] >>\nendobj\n" +
            "xref\n0 4\n0000000000 65535 f\n0000000009 00000 n\n" +
            "0000000058 00000 n\n0000000115 00000 n\n" +
            "trailer\n<< /Size 4 /Root 1 0 R >>\nstartxref\n190\n%%EOF";

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(pdf.getBytes());
        fos.close();

        System.out.println("Created dummy PDF: " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    // ── Upload 3 files: 2 existing slots + 1 after clicking Add Documents ─
    private void uploadDocuments() {
        try {
            String pdf1 = createDummyPdf("test_doc_1.pdf");
            String pdf2 = createDummyPdf("test_doc_2.pdf");
            String pdf3 = createDummyPdf("test_doc_3.pdf");

            // ── Slot 1 & 2 — already visible on page ─────────────────────
            List<WebElement> fileInputs = driver.findElements(
                By.cssSelector("input[type='file']")
            );

            if (fileInputs.size() >= 1) {
                makeVisible(fileInputs.get(0));
                fileInputs.get(0).sendKeys(pdf1);
                System.out.println("Uploaded file 1 to slot 1");
                Waits.pause(1000);
            }

            if (fileInputs.size() >= 2) {
                makeVisible(fileInputs.get(1));
                fileInputs.get(1).sendKeys(pdf2);
                System.out.println("Uploaded file 2 to slot 2");
                Waits.pause(1000);
            }

            // ── Slot 3 — click "+ Add Documents" to reveal third slot ─────
            try {
                WebElement addDocsBtn = driver.findElement(By.xpath(
                    "//*[contains(text(),'Add Documents') or contains(text(),'Add Document')]"
                ));
                scrollTo(addDocsBtn);
                click(addDocsBtn);
                Waits.pause(1000);
                System.out.println("Clicked Add Documents");

                // Re-fetch all inputs — new one should now be in the list
                List<WebElement> updatedInputs = driver.findElements(
                    By.cssSelector("input[type='file']")
                );

                if (updatedInputs.size() >= 3) {
                    makeVisible(updatedInputs.get(2));
                    updatedInputs.get(2).sendKeys(pdf3);
                    System.out.println("Uploaded file 3 to slot 3");
                    Waits.pause(1000);
                } else {
                    System.out.println("Third slot not found after clicking Add Documents. Inputs found: " + updatedInputs.size());
                }

            } catch (Exception e) {
                System.err.println("Could not click Add Documents: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("File upload failed: " + e.getMessage());
        }
    }

    // ── Make hidden file input interactable via JS ────────────────────────
    private void makeVisible(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].style.width    = '1px';" +
            "arguments[0].style.height   = '1px';" +
            "arguments[0].style.opacity  = '1';" +
            "arguments[0].style.position = 'relative';" +
            "arguments[0].style.clip     = 'auto';" +
            "arguments[0].style.clipPath = 'none';" +
            "arguments[0].removeAttribute('tabindex');",
            el
        );
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

    private void selectPreferredCountry(String countryText) {
        try {
            scrollTo(preferredCountriesCombobox);
            click(preferredCountriesCombobox);
            Waits.pause(1200);

            WebElement option = driver.findElement(By.xpath(
                "//span[normalize-space(text())='" + countryText + "']"
            ));
            scrollTo(option);
            click(option);
            Waits.pause(500);
            System.out.println("Selected Preferred Country: " + countryText);

            driver.findElement(By.tagName("body"))
                  .sendKeys(org.openqa.selenium.Keys.ESCAPE);
            Waits.pause(400);

        } catch (Exception e) {
            System.err.println("Could not select country '" + countryText + "': " + e.getMessage());
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

    public void clickSubmit() {
        try {
            scrollTo(submitBtn);
            click(submitBtn);
        } catch (Exception e) {
            jsClick(submitBtn);
        }
        Waits.pause(2000);
        System.out.println("Verification submitted. URL: " + currentUrl());
    }
}