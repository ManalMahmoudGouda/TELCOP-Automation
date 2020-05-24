package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.xml.ws.WebEndpoint;

public class HomePage extends PageBase {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage(WebDriver driver, String appURL) {
        super(driver, appURL);
    }

    @FindBy(xpath = "//span[contains(@class,'admin-name')]")
    public WebElement userName;
    @FindBy(xpath = "//a[text() = ' Course ']")
    public WebElement course;
    @FindBy(xpath = "//a[text() = ' New Course ']")
    public WebElement newCourse;

    @FindBy(xpath = "//i[contains(@class, 'fa fa-sign-out')]")
    public WebElement signoutAnchor;

    @FindBy(xpath = "//i[contains(@class, 'fa fa-user')]")
    public WebElement profileAnchor;

    @FindBy(xpath = "//i[contains(@class, 'fa fa-angle-down mg-l-1')]")
    public WebElement profileDropDownList;

    public boolean userNameIsDisplayed() {
        return userName.isDisplayed();
    }

    public void signOut() {
        profileDropDownList.click();
        signoutAnchor.click();
    }

    public void signOutIfLoggedIn(){
        driver.navigate().to(this.URL + "/");
        try{
            if(userName.isDisplayed()){
                profileDropDownList.click();
                signoutAnchor.click();
            }
        } catch (NoSuchElementException ex){
            return;
        }
    }
}
