package pages;

import com.github.javafaker.Faker;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewCoursePage extends PageBase {
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
    public WebElement courseLanguage;

    @FindBy(id = "expectedFinishedDate")
    public WebElement startDate;


    @FindBy(id = "description")
    public WebElement description;

    @FindBy(xpath = "//button[@class=\"btn btn-success\"]")
    public WebElement btnCreate;

    @FindBy(className = "btn btn-danger")
    public WebElement btnClear;

    public void setFutureDateForExpectedFinishedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String expectedFinishDate = sdf.format(new Date((new Date().getTime() + 172800)));
        this.startDate.sendKeys(expectedFinishDate);
    }

    public String setRandomCourseTitle() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
        Faker faker = new Faker();
        String courseTitle = faker.educator().course() + sdf.format(new Date());
        setText(this.courseTitle, courseTitle);
        return courseTitle;
    }

}
