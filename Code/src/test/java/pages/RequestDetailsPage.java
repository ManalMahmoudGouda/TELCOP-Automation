package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RequestDetailsPage extends PageBase {
    public RequestDetailsPage(WebDriver driver) {
        super(driver);
    }
    @FindBy(xpath = "//p[text() = 'Request Details']")
    WebElement  requestDetailsTxt;


    public boolean requestDetailsIsDisplayed(){
        return requestDetailsTxt.isDisplayed();
//        if(requestDtailsTxt.isDisplayed()) {
//            return true;
//        }
//        else {
//            return false;
//        }
    }

}
