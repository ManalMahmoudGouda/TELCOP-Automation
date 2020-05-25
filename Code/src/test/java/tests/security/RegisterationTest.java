package tests.security;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.security.LoginPage;
import pages.security.RegistrationPage;
import tests.TestBase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterationTest extends TestBase {
    public RegisterationTest() throws IOException, ParseException {
    }

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = "test-Data.json";
    }


    @Test(priority = 1)
    public void registerNewUser() throws InterruptedException, IOException, ParseException {
        driver.navigate().to(this.getAppURL());

        LoginPage login = new LoginPage(driver);
        login.clickRegisterLink();

        RegistrationPage register = new RegistrationPage(driver);

        JSONObject _register = (JSONObject) this.data.get("register");
        JSONObject testCase = (JSONObject) _register.get("registerNewUser");

        SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
        String num = sdf.format(new Date());
        String username = testCase.get("userName") + num;

        String password = (String) testCase.get("password");

        register.setFullName((String) testCase.get("fullName"));
        register.setUsername(username);
        register.setPassword(password);
        register.setConfirmPassword((String) testCase.get("confirmPassword"));
        register.setEmailTxt((String) testCase.get("email"));
        register.toggleSubscribe();
        register.clickRegisterBtn();

        Thread.sleep(3000);
        Assert.assertTrue(login.userNameText.isDisplayed(), "Register doesn't navigate to Login Page");
    }


    @Test(priority = 2)
    public void registerInvalidUserData() throws InterruptedException {
        driver.navigate().to(this.getAppURL());

        LoginPage login = new LoginPage(driver);
        login.clickRegisterLink();

        JSONObject _register = (JSONObject) this.data.get("register");
        JSONArray testCaseArr = (JSONArray) _register.get("registerInvalidUserData");

        for (int i = 0; i < testCaseArr.size(); i++) {
            JSONObject user = (JSONObject) testCaseArr.get(i);

            // SoftAssert sa = new SoftAssert();
            RegistrationPage register = new RegistrationPage(driver);
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
            register.resetForm();
//            Thread.sleep(5000);
        }
    }
}

