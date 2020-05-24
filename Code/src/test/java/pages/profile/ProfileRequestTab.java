package pages.profile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProfileRequestTab extends UserProfilePage {

    public ProfileRequestTab(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "reqTitle")
    public WebElement reqTitleTxt;

    @FindBy(id = "searchBtn")
    public WebElement searchBtn;

    @FindBy(id = "clearBtn")
    public WebElement clearBtn;
}
