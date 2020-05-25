package pages.security;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.PageBase;

public class LoginPage extends PageBase {
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage(WebDriver driver, String appURL) {
        super(driver, appURL);
    }

    @FindBy(xpath = "//a[@href=\"/register\"]")
    public WebElement registerLink;
    @FindBy(id = "username")
    public WebElement userNameText;
    @FindBy(id = "password")
    public WebElement passwordText;

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

    public void login(String userName, String password) {

        driver.navigate().to(this.URL + "/login");
        setUserName(userName);
        setPassword(password);
        clickLogin();
    }
}

