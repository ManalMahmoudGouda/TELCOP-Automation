package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import javax.xml.ws.WebEndpoint;

public class HomePage extends PageBase {
    public HomePage(WebDriver driver) {
        super(driver);
    }
    @FindBy(xpath = "//span[contains(@class,'admin-name')]")
    WebElement userName;
    @FindBy(xpath = "//a[text() = ' Course ']")
    public WebElement course;
    @FindBy(xpath = "//a[text() = ' New Course ']")
    public WebElement newCourse;
   // @FindBy(className = "")
    @FindBy(xpath= "//i[contains(@class, 'fa fa-sign-out')]")
    public WebElement btnSignOut;
    @FindBy(xpath= "//i[contains(@class, 'fa fa-angle-down mg-l-1')]")
    //i[contains(@class, 'fa fa-angle-down mg-l-1')]
    public WebElement profileDropDownList;

    public boolean userNameIsDisplayed(){

        if( userName.isDisplayed())
            return true;
        else
            return false;

     }

     public void signOut(){
        profileDropDownList.click();
        btnSignOut.click();
     }

}
