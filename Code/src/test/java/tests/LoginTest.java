package tests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RegistrationPage;

import javax.xml.soap.SAAJResult;

public class LoginTest extends TestBase {
    LoginPage login;
    HomePage homePage;

    @Override
    void setJSONFileName() {
        this.FILE_NAME = "test-data-login.json";
    }

    @Test
    public void loginUsingValidData() throws InterruptedException {

        login = new LoginPage(driver);

        driver.navigate().to("http://localhost:9120/login");

        JSONObject _login = (JSONObject) this.data.get("login");
        JSONObject loginTestCase = (JSONObject) _login.get("loginWithValidData");
        login.setUserName((String) loginTestCase.get("userName"));
        login.setPassword((String) loginTestCase.get("password"));
        login.clickLogin();
        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.userNameIsDisplayed(), "user name isn't displayed");
    }

    @Test
    public void loginUsingInvalidData() throws InterruptedException {

        login = new LoginPage(driver);

        driver.navigate().to("http://localhost:9120/login");
        JSONObject _login = (JSONObject) this.data.get("login");
        JSONArray testCaseArr = (JSONArray) _login.get("loginWithInvalidData");
        for (int i = 0; i < testCaseArr.size(); i++) {
            JSONObject user = (JSONObject) testCaseArr.get(i);
            login = new LoginPage(driver);
            login.setUserName((String) user.get("userName"));
            login.setPassword((String) user.get("password"));
            login.clickLogin();
            String fieldToBeValidated = (String) user.get("fieldToBeValidated");
            switch (fieldToBeValidated) {
                case "password":
                    Assert.assertTrue(login.isErrorMSgPasswordIsDisplayed(), "error msg for incorrect password isn't displayed ");
                    break;
                case "userName":
                    Assert.assertTrue(login.isErrorMSgUserNameIsDisplayed(), "error msg for incorrect user  isn't displayed");
                    break;
                case "allFields":
                    Assert.assertTrue(login.isErrorMSgPasswordIsDisplayed(), "error msg for incorrect password isn't displayed ");
                    Assert.assertTrue(login.isErrorMSgUserNameIsDisplayed(), "error msg for incorrect user  isn't displayed");
                    break;
            }
            Thread.sleep(5000);
            login.resetForm();


        }
    }}
