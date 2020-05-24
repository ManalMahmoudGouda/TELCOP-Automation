package pages.profile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import pages.PageBase;

import java.util.List;

public class UserProfilePage extends PageBase {

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//ul[@class='tabs']/descendant::a[contains(text(), 'Requests')]")
    public WebElement requestsTab;

    @FindBy(xpath = "//table[@id='requestListTbl']//tbody//tr")
    public List<WebElement> requestsRows;
}
