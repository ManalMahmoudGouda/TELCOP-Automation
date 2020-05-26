package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageBase {
    protected String URL;
    protected WebDriver driver;

    public PageBase(WebDriver driver, String appURL) {
        this.driver = driver;
        this.URL = appURL;
        PageFactory.initElements(driver, this);
    }

    public PageBase(WebDriver driver) {
        this(driver, null);
    }

    public void setInputValue(WebElement input, String value) {
        WebDriverWait wait = new WebDriverWait(driver, 2, 500);
        wait.until(ExpectedConditions.visibilityOf(input));
        input.sendKeys(value);
    }

    public void clickBtn(WebElement btn) {
        WebDriverWait wait = new WebDriverWait(driver, 2, 500);
        wait.until(ExpectedConditions.visibilityOf(btn));
        btn.click();
    }

    public void selectOption(WebElement selectElement, String value) {
        if (value != null) {
            WebDriverWait wait = new WebDriverWait(driver, 2, 500);
            wait.until(ExpectedConditions.visibilityOf(selectElement));

            Select statusDropdown = new Select(selectElement);
            statusDropdown.selectByVisibleText(value);
        }
    }

    public void waitUntilVisible(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver, 2, 500);
        wait.until(ExpectedConditions.visibilityOf(element));
    }
}
