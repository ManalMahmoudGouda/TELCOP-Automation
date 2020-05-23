package tests.course;

import database.CreateCourseDB;
import org.fluttercode.datafactory.impl.DataFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import tests.TestBase;
import util.JDBCAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateCourseTC extends TestBase {

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = "course/create-course-data.json";
    }

    private LoginPage login;
//    private NewCoursePage NewCoursePage coursePage;
    private HomePage home;
    private RequestDetailsPage details;

    private void loginToCreateCoursePage(){
        login = new LoginPage(driver, this.getAppURL());
        JSONObject loginCredentials = (JSONObject) this.data.get("instructorUser");
        login.login((String) loginCredentials.get("userName"), (String) loginCredentials.get("password"));

        home = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.course));
        PageBase.clickButton(home.course);
        PageBase.clickButton(home.newCourse);
    }

    @Test()
    public void createNewCourseUsingValidData() throws Exception {
        this.loginToCreateCoursePage();

        JSONObject tcData = (JSONObject) this.data.get("createCourse");
        NewCoursePage coursePage = new NewCoursePage(driver);
        PageBase.setText(coursePage.courseTitle, (String) tcData.get("courseTitle"));
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguauge, (String) tcData.get("courseLanguage"));
        PageBase.setText(coursePage.startDate, (String) tcData.get("startDate"));
        PageBase.setText(coursePage.description, (String) tcData.get("description"));
        PageBase.clickButton(coursePage.btnCreate);

        details = new RequestDetailsPage(driver);
        Assert.assertTrue(details.requestDetailsIsDisplayed(), "Request Details isn't displayed");
        home.signOut();
    }

    @Test(dependsOnMethods = {"createNewCourseUsingValidData"})
    public void validateDuplicateCourseWithSameInstructor() throws Exception {
        this.loginToCreateCoursePage();

        JSONObject tcData = (JSONObject) this.data.get("validateDuplicateCourseWithSameInstructor");
        NewCoursePage coursePage = new NewCoursePage(driver);
        PageBase.setText(coursePage.courseTitle, (String) tcData.get("courseTitle"));
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguauge, (String) tcData.get("courseLanguage"));
        PageBase.setText(coursePage.startDate, (String) tcData.get("startDate"));
        PageBase.setText(coursePage.description, (String) tcData.get("description"));
        PageBase.clickButton(coursePage.btnCreate);

        this.assertAlertError((String) tcData.get("expectedErrMsg"));
        takeScreenshot(driver, "CourseTitleNotUnique");
    }

    @Test()
    public void createNewCourseUsingInValidData() throws Exception {
        this.loginToCreateCoursePage();

        JSONArray tcDataArray = (JSONArray) this.data.get("CreateCourseWithInvalidData");
        for (int i = 0; i < tcDataArray.size(); i++) {
            JSONObject courseData = (JSONObject) tcDataArray.get(i);

            NewCoursePage coursePage = new NewCoursePage(driver);
            PageBase.setText(coursePage.courseTitle, (String) courseData.get("courseTitle"));
            PageBase.setText(coursePage.duration, (String) courseData.get("duration"));
            PageBase.setText(coursePage.categoryName, (String) courseData.get("categoryName"));
            PageBase.selectOption(coursePage.courseType, (String) courseData.get("courseType"));
            PageBase.selectOption(coursePage.courseLevel, (String) courseData.get("courseLevel"));
            PageBase.selectOption(coursePage.courseLanguauge, (String) courseData.get("courseLanguage"));
            PageBase.setText(coursePage.startDate, (String) courseData.get("startDate"));
            PageBase.setText(coursePage.description, (String) courseData.get("description"));
            PageBase.clickButton(coursePage.btnCreate);

            String errorMsgIDs = (String) courseData.get("errorMsgIds");
            String[] errorMsgIDAry = errorMsgIDs.split(",");

            for (String errorMsgID : errorMsgIDAry)
                this.assertElementErrorMsg(errorMsgID);

            takeScreenshot(driver, "newCourse");
        }
    }

    @Test()
    public void validateMandatoryField() throws Exception {
        this.loginToCreateCoursePage();

        NewCoursePage coursePage = new NewCoursePage(driver);
        PageBase.clickButton(coursePage.btnCreate);

        JSONObject mandatoryData = (JSONObject) this.data.get("validateMandatoryField");
        String errorMsgIDs = (String) mandatoryData.get("errorMsgIds");
        String[] errorMsgIDAry = errorMsgIDs.split(",");

        for (String errorMsgID : errorMsgIDAry)
            this.assertElementErrorMsg(errorMsgID);
        takeScreenshot(driver, "Mandatoryfield");
    }

    @Test()
    public void validateDurationFieldWithDecimalValue() throws Exception {
        this.loginToCreateCoursePage();

        JSONObject tcData = (JSONObject) this.data.get("validateDurationField");
        NewCoursePage coursePage = new NewCoursePage(driver);

        PageBase.setText(coursePage.courseTitle, (String) tcData.get("courseTitle"));
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguauge, (String) tcData.get("courseLanguage"));
        PageBase.setText(coursePage.startDate, (String) tcData.get("startDate"));
        PageBase.setText(coursePage.description, (String) tcData.get("description"));
        PageBase.clickButton(coursePage.btnCreate);

        String errorMsgID = (String) tcData.get("errorMsgId");
        this.assertElementErrorMsg(errorMsgID);
        home.signOut();
    }

    @Test()
    public void validateMaxLimitOfCreateNewCourses() throws Exception {
        this.loginToCreateCoursePage();
        JSONObject tcData = (JSONObject) this.data.get("tc-validateMaxLimitOfCreateNewCourses");

        CreateCourseDB db = new CreateCourseDB();
        String configID = (String) tcData.get("maxLimitConfigurationID");
        Integer maxLimit = db.getMaxLimitOfNewCourseConfiguration(this.jsonConfig, configID);

        JSONObject loginCredentials = (JSONObject) this.data.get("instructorUser");
        Integer count = db.countOfUnCompletedCourses(jsonConfig, (String) loginCredentials.get("userID"));

        Integer diff = maxLimit - count;
        DataFactory dataFactory = new DataFactory();

        NewCoursePage coursePage = new NewCoursePage(driver);

        for(int i=0; i<diff; i++){
            // Random Course Title
            String courseTitle = dataFactory.getRandomWord(5, 150);
            PageBase.setText(coursePage.courseTitle, courseTitle);

            PageBase.setText(coursePage.duration, "50");
            PageBase.setText(coursePage.categoryName, "Testing Category");
            PageBase.selectOption(coursePage.courseType, "Practical");
            PageBase.selectOption(coursePage.courseLevel, "Intermediate");
            PageBase.selectOption(coursePage.courseLanguauge, "English");
            coursePage.setExpectedFinishDate();

            PageBase.clickButton(coursePage.btnCreate);
        }

        // Random Course Title
        String courseTitle = dataFactory.getRandomWord(25, 150);
        PageBase.setText(coursePage.courseTitle, courseTitle);

        PageBase.setText(coursePage.duration, "50");
        PageBase.setText(coursePage.categoryName, "Testing Category");
        PageBase.selectOption(coursePage.courseType, "Practical");
        PageBase.selectOption(coursePage.courseLevel, "Intermediate");
        PageBase.selectOption(coursePage.courseLanguauge, "English");
        coursePage.setExpectedFinishDate();

        PageBase.clickButton(coursePage.btnCreate);

        this.assertAlertError((String) tcData.get("Instructor has reached his max limit of creating new Courses"));
    }
}
