package pages.request;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.PageBase;

public class RequestDetailsPage extends PageBase {
    public RequestDetailsPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(xpath = "//a[@class='side-title active']")
    public WebElement requestTitleHeader;


    public boolean requestDetailsIsDisplayed(){
        return requestTitleHeader.isDisplayed();
//        if(requestDtailsTxt.isDisplayed()) {
//            return true;
//        }
//        else {
//            return false;
//        }
    }

}
