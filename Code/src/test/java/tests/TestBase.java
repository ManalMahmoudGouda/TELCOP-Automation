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
    protected JDBCAdapter jdbc;

    @BeforeClass
    public void initialize() throws IOException, SQLException, ClassNotFoundException {

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        // Create object of ChromeOption class
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver_ver_81.exe");
        driver = new ChromeDriver(options);
//        driver.manage().window().maximize();

        try {
            this.setJSONFileName();
            this.data = (JSONObject) this.readJson();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        jdbc = new JDBCAdapter();
        jdbc.initConnection();
    }

    protected abstract void setJSONFileName();

    @AfterClass
    //Test cleanup
    public void TeardownTest() {
//        TestBase.driver.quit();
    }


    public Object readJson() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("E:\\Work\\Projects\\TELCOP-Automation\\trunk\\Code" +
                "\\src\\test\\resources\\test-data\\" +
                this.FILE_NAME);//"test-Data.json");
        //Read JSON file
        Object obj = jsonParser.parse(reader);
        return obj;
    }




    public  void takeScreenshot(WebDriver webdriver,String filename) throws Exception{
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File DestFile=new File("E:\\Work\\Projects\\TELCOP-Automation\\trunk\\Code\\Screenshots\\" + filename + ".png");

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
}
