package pages;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PageBase {
    protected String URL = "http://localhost:9120/login";
    protected WebDriver driver;

    public PageBase(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        driver.manage().window().fullscreen();

    }

    public static void   setText(WebElement text, String value) {

        text.sendKeys(value);

    }

    public  static void setPassword(WebElement password, String value) {
        password.sendKeys(value);

    }

    public static void clickButton(WebElement btn){
        btn.click();
    }
    public static void selectOption(WebElement selectElement, String value){
        if(value != null) {
            Select statusDropdown = new Select(selectElement);
            statusDropdown.selectByVisibleText(value);
        }

    }

}
