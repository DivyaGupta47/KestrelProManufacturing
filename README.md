# 🧪 KestrelPro Manufacturing – QA Automation Framework

KestrelPro is a hybrid automation framework for validating manufacturing workflows via both **API and UI testing**. It uses **TestNG, Selenium, RestAssured**, and **Ory Kratos** for authenticated testing. CI/CD is fully integrated with **GitHub Actions**, and test results are published in both **HTML and PDF** using ExtentReports.

---

## ⚙️ Tech Stack

- **Test Framework**: TestNG  
- **UI Testing**: Selenium WebDriver (Java)  
- **API Testing**: RestAssured  
- **Authentication**: Ory Kratos  
- **Reports**: ExtentReports (HTML & PDF)  
- **CI/CD**: GitHub Actions  
- **Build Tool**: Maven

---

## ✅ Features

- 🔐 Secure session-based testing with Ory Kratos  
- 🔄 Complete Admin + Assignee test flows  
- 📄 Combined HTML + PDF reports (Tests + Dashboard tabs)  
- 📊 Graphs, timelines, pass/fail summaries  
- 🔁 CI on push, PR, and scheduled runs (daily)

---

## 📁 Folder Structure

```
KestrelPro/
├── src/main/java/pages/        → Page Objects  
├── src/test/java/tests/        → TestNG test classes  
├── utils/, listeners/          → Helpers, Reporting  
├── test-output/                → HTML + PDF reports  
└── .github/workflows/ci.yml    → GitHub Actions workflow
```

---

## 🚀 Run Tests Locally

```bash
mvn clean test
```

---

## 🧾 CI/CD Output

- ✅ `KestrelPro_FinalReport.pdf` – Merged PDF (Tests + Dashboard)
- 📁 `test-output/` – Full HTML report with screenshots/logs

---

## 👤 Contributors

- Divya Gupta  
- Deepak Jodhwani  
- QA Team – 47Billion