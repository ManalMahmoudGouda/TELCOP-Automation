package tests.security;

import database.UsersDB;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.security.LoginPage;
import pages.security.RegistrationPage;
import tests.TestBase;

import java.io.IOException;
import java.sql.SQLException;

@Test
public class LoginTest extends TestBase {
    private LoginPage login;

    public LoginTest() throws SQLException, ClassNotFoundException, IOException, ParseException {
        super();
    }


    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = "test-data-login.json";
    }

    @BeforeMethod
    public void checkIsUserExist() throws SQLException, ClassNotFoundException {
        System.out.println("Before Method");
        JSONObject _login = (JSONObject) this.data.get("login");
        JSONObject loginTestCase = (JSONObject) _login.get("loginWithValidData");
        String username = (String) loginTestCase.get("userName");
        String password = (String) loginTestCase.get("password");

        UsersDB userDB = new UsersDB(this.jsonConfig);
        Boolean isUserExist = userDB.checkIsUserExist(username);

        if(!isUserExist){
            //Create User using Register
            driver.navigate().to(this.getAppURL() + "/register");
            RegistrationPage register = new RegistrationPage(driver);
            register.setFullName("Ahmed Mater");
            register.setUsername(username);
            register.setPassword(password);
            register.setConfirmPassword(password);
            register.setEmailTxt("ahmed.motair@gmail.com");

            register.clickRegisterBtn();

        }
    }


    @Test
    public void loginUsingValidData() throws Exception {
        System.out.println("Test Execution");
        login = new LoginPage(driver);

        driver.navigate().to(this.getAppURL() + "/login");

        JSONObject _login = (JSONObject) this.data.get("login");
        JSONObject loginTestCase = (JSONObject) _login.get("loginWithValidData");
        login.setUserName((String) loginTestCase.get("userName"));
        login.setPassword((String) loginTestCase.get("password"));

        login.clickLogin();
//        HomePage homePage = new HomePage(driver);
        this.takeScreenshot(driver, "Login-Success");
//        Assert.assertTrue(homePage.userNameIsDisplayed(), "user name isn't displayed");
    }

    @Test
    public void loginUsingInvalidData() throws InterruptedException {

        login = new LoginPage(driver);

        driver.navigate().to(this.getAppURL() + "/login");
        JSONObject _login = (JSONObject) this.data.get("login");
        JSONArray testCaseArr = (JSONArray) _login.get("loginWithInvalidData");
        for (int i = 0; i < testCaseArr.size(); i++) {
            JSONObject user = (JSONObject) testCaseArr.get(i);
            login = new LoginPage(driver);
            login.setUserName((String) user.get("userName"));
            login.setPassword((String) user.get("password"));
            login.clickLogin();
            String fieldToBeValidated = (String) user.get("fieldToBeValidated");

            WebDriverWait webDriverWait = new WebDriverWait(driver, 2, 500);

            switch (fieldToBeValidated) {
                case "password":
                    webDriverWait.until(ExpectedConditions.visibilityOf(login.errorMsgPassword));
                    Assert.assertTrue(login.isErrorMSgPasswordIsDisplayed(), "error msg for incorrect password isn't displayed ");
                    break;
                case "userName":
                    webDriverWait.until(ExpectedConditions.visibilityOf(login.errorMsguserName));
                    Assert.assertTrue(login.isErrorMSgUserNameIsDisplayed(), "error msg for incorrect user  isn't displayed");
                    break;
                case "allFields":
                    webDriverWait.until(ExpectedConditions.visibilityOf(login.errorMsgPassword));
                    webDriverWait.until(ExpectedConditions.visibilityOf(login.errorMsguserName));
                    Assert.assertTrue(login.isErrorMSgPasswordIsDisplayed(), "error msg for incorrect password isn't displayed ");
                    Assert.assertTrue(login.isErrorMSgUserNameIsDisplayed(), "error msg for incorrect user  isn't displayed");
                    break;
            }
            Thread.sleep(5000);
            login.resetForm();


        }
    }





}
