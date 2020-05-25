package tests.security;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.security.LoginPage;
import pages.security.RegistrationPage;
import tests.TestBase;

import java.io.FileReader;
import java.io.IOException;

public class RegistrationTestUsingCsv extends TestBase {

    RegistrationPage register;
    LoginPage login;

    public RegistrationTestUsingCsv() throws IOException, ParseException {
    }

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = "test-Data.json";
    }

    CSVReader reader;

    @Test(priority = 1)
    public void registerInvalidUserData() throws InterruptedException, IOException, CsvValidationException {

        login = new LoginPage(driver);
        register = new RegistrationPage(driver);

        driver.navigate().to(this.getAppURL() + "/login");
        login.clickRegisterLink();
        //get path of csv file
        String CSV_file = System.getProperty("user.dir") + "\\src\\test\\resources\\test-data\\Login-test-data.csv";

        reader = new CSVReader(new FileReader(CSV_file));
        String[] csvCell;
        while ((csvCell = reader.readNext()) != null) {
            String fullName = csvCell[0];
            String userName = csvCell[1];
            String password = csvCell[2];
            String ConfirmPassword = csvCell[3];
            String email = csvCell[4];
            String fieldToBeVlidate = csvCell[5];
            register.setUsername(userName);
            register.setFullName(fullName);
            register.setPassword(password);
            register.setConfirmPassword(ConfirmPassword);
            register.setEmailTxt(email);

            register.clickRegisterBtn();
            switch (fieldToBeVlidate) {
                case "UserName":
                    Assert.assertTrue(register.isEmailAndUserNameUnique(), "Username Error Message isn't displayed");
                    Assert.assertEquals(register.getErrorAlertText(), "Username or Email already existed", "error msg for unique name isn't correct");
                    register.closeAlert();
                    break;
                case "Email":
                    Assert.assertTrue(register.isEmailAndUserNameUnique(), "Email Error Message isn't displayed");
                    Assert.assertEquals(register.getErrorAlertText(), "Username or Email already existed", "error msg for unique email isn't correct");
                    register.closeAlert();
                    break;

            }


            Thread.sleep(5000);
            register.resetForm();
        }
    }

}





