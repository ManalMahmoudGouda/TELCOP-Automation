package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends PageBase {
    public HomePage(WebDriver driver) {
        super(driver);
    }
    @FindBy(xpath = "//span[contains(@class,'admin-name')]")
    WebElement userName;
     public boolean userNameIsDisplayed(){

        if( userName.isDisplayed())
            return true;
        else
            return false;

     }
}
