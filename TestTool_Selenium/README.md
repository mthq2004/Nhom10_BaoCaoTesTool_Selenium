# Selenium test suite for ProjectThymeleafSpringboot

This small Selenium/TestNG suite exercises core flows of the sample Thymeleaf Spring Boot app (login, product management, cart/checkout).

Assumptions
- The application is running locally at http://localhost:8080 (changeable via -DbaseUrl)
- There exist two accounts (configurable via system properties): an admin and a customer. Defaults used by tests:
  - admin: username=admin password=admin
  - customer: username=customer password=customer

You can override values when running tests, for example on Windows cmd.exe:

    mvn -f TestTool_Selenium/pom.xml test -DbaseUrl=http://localhost:8080 -Dadmin.username=admin -Dadmin.password=secret -Dcustomer.username=cu -Dcustomer.password=cu

Notes
- Tests follow the simple style from the sample `TestMain.java` (Thread.sleep). They are intended as a starting point. Replace sleeps with explicit waits if needed.
- If users or roles don't exist, create them in the app or change system properties to real accounts.

Files added
- `BaseTest.java` — WebDriver setup/teardown and login helper.
- `HomeTest.java` — navigation from home to product list.
- `AuthTest.java` — login/logout verification for admin.
- `ProductTest.java` — add a product as admin, verify in list.
- `CartTest.java` — add to cart as customer and checkout (verify success).

Next steps
- Add more tests (category, customer, order details) and convert sleeps to WebDriverWait for robustness.
- Optionally integrate with CI and a headless browser profile.
