package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserProfilePage extends PageBase {

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//ul[@class='tabs']/descendant::a[contains(text(), 'Requests')]")
    public WebElement requestsTab;
}
