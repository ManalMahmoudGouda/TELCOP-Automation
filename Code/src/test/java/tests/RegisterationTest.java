package tests;

import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import pages.RegistrationPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterationTest extends TestBase {

    RegistrationPage register;
    LoginPage login;


    @Test(priority = 1)
    public void registerNewUser() throws InterruptedException, IOException, ParseException {
        login = new LoginPage(driver);
        driver.navigate().to("http://localhost:9120/login");
        login.clickRegisterLink();

        register = new RegistrationPage(driver);

        JSONObject _register = (JSONObject) this.data.get("register");
        JSONObject testCase = (JSONObject) _register.get("registerNewUser");

        register.setFullName((String) testCase.get("fullName"));
        register.setUsername((String) testCase.get("userName"));
        register.setPassword((String) testCase.get("password"));
        register.setConfirmPassword((String) testCase.get("confirmPassword"));
        register.setEmailTxt((String) testCase.get("email"));
        register.toggleSubscribe();
        register.clickRegisterBtn();
        // driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
        Thread.sleep(30000);
        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:9120/login",
                "Register doesn't navigate to Login Page");
    }


    @Test(priority = 2)
    public void registerInvalidUserData() throws InterruptedException {
        login = new LoginPage(driver);
        driver.navigate().to("http://localhost:9120/login");
        login.clickRegisterLink();


        JSONObject _register = (JSONObject) this.data.get("register");
        JSONArray testCaseArr = (JSONArray) _register.get("registerInvalidUserData");
        /*
        for(Object testData: testCaseData.toArray()){
            JSONObject _testData = (JSONObject) testData;

        }
        */


        for (int i = 0; i < testCaseArr.size(); i++) {
            JSONObject user = (JSONObject) testCaseArr.get(i);

            // SoftAssert sa = new SoftAssert();
            register = new RegistrationPage(driver);
            register.setFullName((String) user.get("fullName"));
            register.setUsername((String) user.get("userName"));
            register.setPassword((String) user.get("password"));

            register.setConfirmPassword((String) user.get("confirmPassword"));
            register.setEmailTxt((String) user.get("email"));
            register.toggleSubscribe();
            register.clickRegisterBtn();

            String fieldToBeValidated = (String) user.get("fieldToBeValidated");
            switch (fieldToBeValidated){
                case "username":
                    Assert.assertTrue(register.isUsernameErrorDisplayed(), "Username Error Message isn't displayed");
                    break;
                case "email":
                    Assert.assertTrue(register.isEmailErrorDisplayed(),"Email Error Message isn't displayed");
                    break;
                case "allFields":
                    Assert.assertTrue(register.isEmailErrorDisplayed(),"Email Error Message isn't displayed");
                    Assert.assertTrue(register.isUsernameErrorDisplayed(), "Username Error Message isn't displayed");
                    break;


            }
            //hard assert
//        Assert.assertTrue(register.isFullNameErrorDisplayed(), "Full name Error Message isn't displayed");
            Thread.sleep(5000);
            register.resetForm();
        }

    }

    @Override
    void setJSONFileName() {
        this.FILE_NAME = "test-Data.json";
    }
}

