# THE Authorized Partner Signup Automation

Automated end-to-end signup flow for [https://authorized-partner.vercel.app](https://authorized-partner.vercel.app) using Java + Selenium + TestNG + Maven.

---

## Project Structure

```
src/
├── main/java/com/theap/signup/
│   ├── pages/          # Page Object Model classes
│   └── utils/          # Helpers (driver, waits, faker, mailinator, report)
└── test/java/com/theap/signup/tests/
    └── SignupTest.java  # Main test class
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java JDK | 21 |
| Maven | 3.8+ |
| Chrome Browser | 149+ |
| ChromeDriver | Must match Chrome version |
| Eclipse IDE | 2023+ (or any Java IDE) |

---

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/<your-username>/theap-signup-automation.git
cd theap-signup-automation
```

### 2. Install dependencies
```bash
mvn clean install -DskipTests
```

### 3. Verify ChromeDriver
Make sure ChromeDriver is either:
- On your system PATH, OR
- Configured in `src/main/java/com/theap/signup/utils/BrowserDriver.java`

### 4. Run the tests
```bash
mvn test
```
Or run via Eclipse:
- Right-click `testng.xml` → Run As → TestNG Suite

---

## How It Works

The test automates a 4-step signup flow:

| Step | Description | Status |
|------|-------------|--------|
| 1 | Account Setup (name, email, phone, password) | ✅ |
| 1b | OTP verification via Mailinator | ✅ |
| 2 | Agency Details (name, role, email, website, address, region) | ✅ |
| 3 | Professional Experience (years, students, focus, metrics, services) | ✅ |
| 4 | Verification & Preferences (registration, countries, institution types, documents) | ✅ |

---

## Test Data

| Field | Value |
|-------|-------|
| Email | Auto-generated each run (e.g. `testuser.06231045@mailinator.com`) |
| OTP | Auto-read from [Mailinator](https://www.mailinator.com) public inbox |
| Name / Phone / Password | Generated via JavaFaker (random each run) |
| Agency Name | Generated via JavaFaker + timestamp |
| Website | `https://www.google.com` |
| Region | Nepal |
| Preferred Country | Australia |
| Upload Files | Auto-generated dummy PDFs in system temp folder |

> **Note:** A new unique email is generated on every test run to avoid "already registered" errors.

---

## Framework & Libraries

| Library | Purpose |
|---------|---------|
| Selenium WebDriver 4.18 | Browser automation |
| TestNG | Test execution and assertions |
| JavaFaker | Random test data generation |
| ExtentReports | HTML test reporting |
| Maven | Build and dependency management |

---

## Running Reports

After test execution, an HTML report is generated at:
```
test-output/ExtentReport.html
```
Open in any browser to view pass/fail results with screenshots.

---

## Notes

- The signup form uses **Radix UI** components (React) — dropdowns render in portals, checkboxes use `role="checkbox"` buttons. Standard Selenium Select/checkbox methods do not work; custom click logic is used.
- IDs in this app are **dynamically generated** by React on every page load — all locators use stable `name` attributes, label text, or ARIA roles instead.
- File uploads use hidden `input[type="file"]` elements made interactable via JavaScript before `sendKeys()`.