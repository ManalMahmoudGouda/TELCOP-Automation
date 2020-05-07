package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.XpiDriverService;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends PageBase {
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//a[@href=\"/register\"]")
    WebElement registerLink;
    @FindBy(id = "username")
    WebElement userNameText;
    @FindBy(id = "password")
    WebElement passwordText;

    @FindBy(xpath = "//div[contains(@class,'alert alert-danger alert-mg-b font-size-14')]")
    WebElement errorMsgPassword;

    @FindBy(xpath = "//div[contains(@class,'alert alert-danger alert-mg-b font-size-14')]")
    WebElement errorMsguserName;
    @FindBy(xpath = "//button[@type='submit']")
    WebElement loginBtn;

    public void clickRegisterLink() {

        registerLink.click();
    }

    public void setUserName(String userName) {
        userNameText.sendKeys(userName);
    }

    public void setPassword(String password) {
        passwordText.sendKeys(password);
    }

    public void clickLogin() {
        loginBtn.click();
    }

    public boolean isErrorMSgPasswordIsDisplayed() {

        if (errorMsgPassword.isDisplayed()) {
            return true;

        } else {
            return false;
        }
    }
    public boolean isErrorMSgUserNameIsDisplayed() {

        if (errorMsguserName.isDisplayed()) {
            return true;

        } else {
            return false;
        }
    }


    public void resetForm() {

        userNameText.clear();
        passwordText.clear();



    }
}

