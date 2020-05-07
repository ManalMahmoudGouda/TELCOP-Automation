package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class RegistrationPage extends PageBase {

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }


    @FindBy(id = "fullName")
    WebElement fullNameTxt;
    @FindBy(id = "username")
    WebElement usernameTxt;
    @FindBy(id = "password")
    WebElement passwordTxt;
    @FindBy(id = "confirmPassword")
    WebElement confirmPasswordTxt;
    @FindBy(id = "email")
    WebElement emailTxt;
    @FindBy(xpath = "//input[@type='checkbox']")
    WebElement subscribeChkBox;
    @FindBy(xpath = "//button[@type='submit']")
    WebElement registerBtn;
    @FindBy(id = "username_ValDiv_pattern_en")
    WebElement errorMsgUserName;
    @FindBy(id = "fullName_ValDiv_pattern_en")
    List<WebElement> fullNameErrMsgList;
    @FindBy(id = "pass_ValDiv_passwordsNotMatching_en")
    WebElement getErrorMsgPassword;
    @FindBy(id = "email_ValDiv_pattern_en")
    WebElement getErrorMsgEmail;
    @FindBy(xpath = "//div[contains(@class,'alert alert-danger alert-mg-b font-size-14')]")
    WebElement getErrormsgUserNameUniqque;
    @FindBy(xpath = "//div[contains(@class,'alert alert-danger alert-mg-b font-size-14')]//button[@type='button']")
    WebElement closeButton;

    public void setFullName(String fullName) {

        fullNameTxt.sendKeys(fullName);
    }

    public void setUsername(String userName) {

        usernameTxt.sendKeys(userName);
    }

    public void setPassword(String password) {

        passwordTxt.sendKeys(password);
    }

    public void setConfirmPassword(String confirmPassword) {

        confirmPasswordTxt.sendKeys(confirmPassword);
    }

    public void setEmailTxt(String email) {

        emailTxt.sendKeys(email);
    }

    public void toggleSubscribe() {

        subscribeChkBox.click();
    }

    public void clickRegisterBtn() {

        registerBtn.click();
    }

    public boolean isUsernameErrorDisplayed() {

        if (errorMsgUserName.isDisplayed()) {
            return true;
        } else {

            return false;
        }

    }

    public boolean isPasswordErrorDisplayed() {

        if (getErrorMsgPassword.isDisplayed()) {
            return true;
        } else {

            return false;
        }

    }

    public boolean isFullNameErrorDisplayed() {
//        System.out.println(fullNameErrMsg);
        if (fullNameErrMsgList.size() != 0 && fullNameErrMsgList.get(0).isDisplayed())
            return true;
        else
            return false;
    }

    public boolean isEmailErrorDisplayed() {
        if (getErrorMsgEmail.isDisplayed())
            return true;
        else
            return false;
    }
    public boolean isEmailAndUserNameUnique(){
        if(getErrormsgUserNameUniqque.isDisplayed())
            return  true;
        else
            return false;


    }
    public String getErrorAlertText(){
        String value =  getErrormsgUserNameUniqque.getText();
        value = value.replaceFirst("×", "").trim();
        return value;
    }
    public void closeAlert(){
        closeButton.click();

    }
    public void resetForm() {

        fullNameTxt.clear();
        usernameTxt.clear();
        passwordTxt.clear();
        confirmPasswordTxt.clear();
        emailTxt.clear();


    }

}