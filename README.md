# VinSmart Future – QA Automation Test

This project is an **automation testing assignment** for the QA Automation position at **VinSmart Future**.  
It demonstrates automated end-to-end (E2E) testing for the [SauceDemo](https://www.saucedemo.com/) web application using **Selenium WebDriver**, **JUnit 5**, and **WebDriverManager**.

---

## 🚀 Tech Stack

| Tool / Library | Version | Purpose |
|----------------|----------|----------|
| Java | 11 | Programming language |
| Maven | 3.x | Build & dependency management |
| Selenium | 4.21.0 | Web automation |
| JUnit 5 (Jupiter) | 5.10.2 | Test framework |
| WebDriverManager | 5.9.2 | Auto-manage browser drivers |
| SLF4J Simple | 2.0.12 | Lightweight logging |
| GitHub Actions | — | CI/CD pipeline for automation tests |

---

## 🧩 Project Structure


---

## 🧪 Test ScenariosScenarios

| #  | Test Case                                   | Description                                                                 | Expected Result                                      |
|----|---------------------------------------------|-----------------------------------------------------------------------------|------------------------------------------------------|
| 1  | **Login with valid user**                   | Enter `standard_user` + `secret_sauce` credentials                          | Redirect to inventory page                           |
| 2  | **Add item to cart and checkout**           | Login, add product, checkout successfully                                  | Display "Thank you for your order!" message          |
| 3  | **(Optional) Locked out user**              | Use `locked_out_user` credentials                                            | Show locked out error message                        |
| 4  | **Login with invalid username**             | Enter invalid username and valid password                                  | Show error message for invalid login                 |
| 5  | **Login with invalid password**             | Enter valid username and invalid password                                  | Show error message for invalid credentials           |
| 6  | **Login with empty username and password**  | Attempt to login with empty username and password                           | Show error message for required fields               |
| 7  | **Login with invalid username format**      | Enter invalid username format (`user!@#$%`) and valid password              | Show error message for invalid username format      |
---

## ⚙️ How to Run Tests Locally

### **1️⃣ Run all tests (default Chrome)**
```bash
mvn clean test

mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox

target/screenshots/
