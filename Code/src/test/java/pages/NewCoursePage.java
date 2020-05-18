package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NewCoursePage extends PageBase{
    public NewCoursePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "corTitle")
  public WebElement courseTitle;

    @FindBy(id = "duration")
    public WebElement duration;

    @FindBy(id = "categoryName")
    public WebElement categoryName;

    @FindBy(id = "corTypeID")
    public WebElement courseType;

    @FindBy(id = "corLevelID")
    public WebElement courseLevel;

    @FindBy(id = "originalLangID")
    public WebElement courseLanguauge;

    @FindBy(id = "expectedFinishedDate")
    public WebElement startDate;


    @FindBy(id = "description")
    public WebElement description;


    @FindBy(xpath = "//button[@class=\"btn btn-success\"]")
    public WebElement btnCreate;

    @FindBy(className = "btn btn-danger")
    public WebElement btnClear;


}
