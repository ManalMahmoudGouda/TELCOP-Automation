package pages.profile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProfileRequestTabPage extends UserProfilePage {

    public ProfileRequestTabPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "reqID")
    public WebElement reqIDtxtInput;

    @FindBy(id = "searchBtn")
    public WebElement searchBtn;

    @FindBy(id = "clearBtn")
    public WebElement clearBtn;
}
