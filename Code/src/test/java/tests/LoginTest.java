package tests;

import database.course.CreateCourseDB;
import database.security.LoginDB;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.RegistrationPage;
import util.JDBCAdapter;

import javax.xml.soap.SAAJResult;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginTest extends TestBase {
    private LoginPage login;
    private HomePage homePage;
    private LoginDB loginDB = new LoginDB(this.jsonConfig);

    public LoginTest() throws SQLException, ClassNotFoundException {
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

        Boolean isUserExist = loginDB.checkIsUserExist(username);

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
        homePage = new HomePage(driver);
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
    }





}
