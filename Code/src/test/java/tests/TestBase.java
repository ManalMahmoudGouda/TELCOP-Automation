package tests;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class TestBase {
    public static WebDriver driver = null;
    public String FILE_NAME;
    JSONObject data;

    @BeforeClass
    public void initialize() throws IOException {

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        // Create object of ChromeOption class
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
        driver = new ChromeDriver(options);
//        driver.manage().window().maximize();

        try {
            this.setJSONFileName();
            this.data = (JSONObject) this.readJson();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    abstract void setJSONFileName();

    @AfterClass
    //Test cleanup
    public void TeardownTest() {
//        TestBase.driver.quit();
    }


    public Object readJson() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("D:\\Work\\Projects\\Test-Automation\\" +
                "trunk\\TeclopTesAutomation\\src\\test\\resources\\test-data\\" +
                this.FILE_NAME);//"test-Data.json");
        //Read JSON file
        Object obj = jsonParser.parse(reader);
        return  obj;
    }
}
