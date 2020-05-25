package pages.security;

import com.github.javafaker.Faker;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import pages.PageBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RegistrationPage extends PageBase {

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    public RegistrationPage(WebDriver driver, String appURL) {
        super(driver, appURL);
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
        return errorMsgUserName.isDisplayed();
    }

    public boolean isPasswordErrorDisplayed() {
        return getErrorMsgPassword.isDisplayed();
    }

    public boolean isFullNameErrorDisplayed() {
        return fullNameErrMsgList.size() != 0 && fullNameErrMsgList.get(0).isDisplayed();
    }

    public boolean isEmailErrorDisplayed() {
        return getErrorMsgEmail.isDisplayed();
    }
    public boolean isEmailAndUserNameUnique(){
        return getErrormsgUserNameUniqque.isDisplayed();
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

    public String registerNewUser(String username, String password){
        WebDriverWait wait=new WebDriverWait(driver, 2, 1000);

        HomePage homePage = new HomePage(driver, this.URL);
        homePage.signOutIfLoggedIn();

        LoginPage loginPage = new LoginPage(driver, this.URL);
        wait.until(ExpectedConditions.visibilityOf(loginPage.registerLink));
        loginPage.clickRegisterLink();

        wait.until(ExpectedConditions.visibilityOf(fullNameTxt));

        SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
        String num = sdf.format(new Date());
        username += num;

        Faker faker = new Faker();
        String fullName = faker.address().firstName() + " " + faker.address().lastName();

        this.setFullName(fullName);
        this.setUsername(username);
        this.setPassword(password);
        this.setConfirmPassword(password);
        this.setEmailTxt(faker.internet().emailAddress());
        this.toggleSubscribe();

        this.clickRegisterBtn();

        loginPage = new LoginPage(driver);
        wait.until(ExpectedConditions.visibilityOf(loginPage.userNameText));
        return username;
    }
}