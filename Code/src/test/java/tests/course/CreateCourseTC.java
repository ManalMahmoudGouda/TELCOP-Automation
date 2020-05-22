package tests.course;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import tests.TestBase;

public class CreateCourseTC extends TestBase {

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = "course\\create-course-data.json";
    }

    private LoginPage login;
    private NewCoursePage coursePage;
    private HomePage home;
    private RequestDetailsPage details;

    @Test()
    public void createNewCourseUsingValidData() throws Exception {
        login = new LoginPage(driver);
        JSONObject tcData = (JSONObject) this.data.get("createCourse");
        login.login((String) tcData.get("userName"), (String) tcData.get("password"));

        home = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.course));
        PageBase.clickButton(home.course);
        PageBase.clickButton(home.newCourse);

        coursePage = new NewCoursePage(driver);
        PageBase.setText(coursePage.courseTitle, (String) tcData.get("courseTitle"));
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguauge, (String) tcData.get("courseLanguage"));
        PageBase.setText(coursePage.startDate, (String) tcData.get("startDate"));
        PageBase.setText(coursePage.description, (String) tcData.get("description"));
        PageBase.clickButton(coursePage.btnCreate);

        //  takeScreenshot(driver, "newCourse");
        details = new RequestDetailsPage(driver);
        Assert.assertTrue(details.requestDetailsIsDisplayed(), "Request Details isn't displayed");
        home.signOut();
    }

    @Test(dependsOnMethods = {"createNewCourseUsingValidData"})
    public void validateDuplicateCourseWithSameInstructor() throws Exception {
        login = new LoginPage(driver);
        JSONObject instructorUser = (JSONObject) this.data.get("instructorUser");
        login.login((String) instructorUser.get("userName"), (String) instructorUser.get("password"));

        home = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.course));
        PageBase.clickButton(home.course);
        PageBase.clickButton(home.newCourse);

        JSONObject tcData = (JSONObject) this.data.get("validateDuplicateCourseWithSameInstructor");
        coursePage = new NewCoursePage(driver);
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
        login = new LoginPage(driver);
        JSONObject tcData = (JSONObject) this.data.get("CreateCourseWithInvalidData");
        JSONObject loginCredentials = (JSONObject) tcData.get("loginCredentials");
        JSONArray data = (JSONArray) tcData.get("data");
        login.login((String) loginCredentials.get("userName"), (String) loginCredentials.get("password"));

        home = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.course));
        PageBase.clickButton(home.course);
        PageBase.clickButton(home.newCourse);
        for (int i = 0; i < data.size(); i++) {

            JSONObject courseData = (JSONObject) data.get(i);

            coursePage = new NewCoursePage(driver);
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
        login = new LoginPage(driver);
        JSONObject instructorUser = (JSONObject) this.data.get("instructorUser");
        login.login((String) instructorUser.get("userName"), (String) instructorUser.get("password"));

        home = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.course));
        PageBase.clickButton(home.course);
        PageBase.clickButton(home.newCourse);

        coursePage = new NewCoursePage(driver);
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

        login = new LoginPage(driver);
        JSONObject instructorUser = (JSONObject) this.data.get("instructorUser");
        login.login((String) instructorUser.get("userName"), (String) instructorUser.get("password"));

        home = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.course));
        PageBase.clickButton(home.course);
        PageBase.clickButton(home.newCourse);

        JSONObject tcData = (JSONObject) this.data.get("validateDurationField");
        coursePage = new NewCoursePage(driver);
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

}
