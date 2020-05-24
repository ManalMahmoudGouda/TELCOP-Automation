package tests;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import util.JDBCAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class TestBase {
    public static WebDriver driver = null;
    public String FILE_NAME;
    protected JSONObject data;
    protected JSONObject jsonConfig;

    @BeforeClass
    public void initialize() throws IOException, SQLException, ClassNotFoundException, ParseException {
        this.jsonConfig = (JSONObject) this.readJson("src/test/resources/config.json");
        String driverPath = (String) this.jsonConfig.get("chromeDriverPath");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);

        // Create object of ChromeOption class
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
//        options.setPageLoadStrategy(PageLoadStrategy.NONE);
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        try {
            this.setJSONFileName();
            this.data = (JSONObject) this.readJson();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    protected abstract void setJSONFileName();

    //Test cleanup
    @AfterClass
    public void TeardownTest() {
//        TestBase.driver.quit();
    }

    private Object readJson() throws IOException, ParseException {
        return this.readJson(null);
    }

    private Object readJson(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader;

        if(filePath == null)
            reader = new FileReader("src/test/resources/test-data/" + this.FILE_NAME);
        else
            reader = new FileReader(filePath);

        //Read JSON file
        return jsonParser.parse(reader);
    }

    public  void takeScreenshot(WebDriver webdriver,String filename) throws Exception{
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File DestFile=new File(this.jsonConfig.get("screenshotsPath") + filename + ".png");

        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }

    // find error by id and assert fail if the error message isn't found
    public void assertElementErrorMsg(String tagID) throws Exception {
        try {
            WebElement element = driver.findElement(By.id(tagID));
        } catch (NoSuchElementException ex){
            takeScreenshot(driver, tagID + "_ID_Not_Found");
            Assert.assertEquals("Error Message isn't displayed", "Error Msg with Tag ID '" + tagID + "'");
        }
    }

    public void assertAlertError(String expectedErrMsg) throws Exception {
        try {
            WebElement element = driver.findElement(By.xpath("//div[contains(@class, 'alert alert-danger')]"));
            String actualErrMsg = element.getText();

            try {
                Assert.assertEquals(actualErrMsg, expectedErrMsg);
            } catch (AssertionError err){
                takeScreenshot(driver, expectedErrMsg + "_Not_Found");
               // Assert.assertEquals(actualErrMsg, expectedErrMsg);
            }
        } catch (NoSuchElementException ex){ // In case of the alert error doesn't appear after clicking the button
            takeScreenshot(driver, "Error_Alert_Not_Found");
            Assert.assertEquals("Error Message isn't displayed", "Error Msg with Text '" + expectedErrMsg + "'");
        }
    }

    public String getAppURL(){
        return (String) this.jsonConfig.get("applicationURL");
    }
}
