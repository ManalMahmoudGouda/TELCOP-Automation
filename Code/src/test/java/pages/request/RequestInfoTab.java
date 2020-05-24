package pages.request;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RequestInfoTab extends RequestDetailsPage{
    public RequestInfoTab(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "reqTitleInfo")
    public WebElement reqTitleInfo;

    @FindBy(id = "reqWorkflowInfo")
    public WebElement reqWorkflowInfo;

    @FindBy(id = "reqStatusInfo")
    public WebElement reqStatusInfo;

    @FindBy(id = "reqPendingOnInfo")
    public WebElement reqPendingOnInfo;

    @FindBy(id = "reqCreatedByInfo")
    public WebElement reqCreatedByInfo;

    @FindBy(id = "reqCreatedOnInfo")
    public WebElement reqCreatedOnInfo;
}
