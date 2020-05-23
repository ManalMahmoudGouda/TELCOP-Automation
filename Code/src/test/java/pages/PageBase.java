package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class PageBase {
    protected String URL;
    protected WebDriver driver;

    public PageBase(WebDriver driver, String appURL) {
        this.driver = driver;
        this.URL = appURL;
        PageFactory.initElements(driver, this);
        driver.manage().window().fullscreen();

    }

    public PageBase(WebDriver driver) {
        this(driver, null);
    }

    public static void setText(WebElement text, String value) {
        text.sendKeys(value);
    }

    public static void setPassword(WebElement password, String value) {
        password.sendKeys(value);
    }

    public static void clickButton(WebElement btn) {
        btn.click();
    }

    public static void selectOption(WebElement selectElement, String value) {
        if (value != null) {
            Select statusDropdown = new Select(selectElement);
            statusDropdown.selectByVisibleText(value);
        }
    }

}
