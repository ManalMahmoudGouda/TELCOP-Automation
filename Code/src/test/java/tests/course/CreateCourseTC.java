package tests.course;

import common.CourseFileNames;
import database.UsersDB;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import pages.*;
import pages.course.NewCoursePage;
import pages.request.RequestDetailsPage;
import pages.security.LoginPage;
import pages.security.RegistrationPage;
import tests.TestBase;
import util.JSONResReader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class CreateCourseTC extends TestBase {
    public UsersDB usersDB;

    public CreateCourseTC() throws SQLException, ClassNotFoundException, IOException, ParseException {
        super();
        usersDB = new UsersDB(this.jsonConfig);
    }

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = CourseFileNames.CREATE_COURSE_TEST_DATA;
    }

    private RequestDetailsPage details;

    private String loginToCreateCoursePage(String username) throws SQLException, IOException, ParseException, InterruptedException {
        String password = JSONResReader.readProperty(this.data, "common.usersPassword");

        if(username == null){
            RegistrationPage registrationPage = new RegistrationPage(driver, this.getAppURL());
            username = registrationPage.registerNewUser("instructor_", password);

            String instructorRoleID = JSONResReader.readProperty(this.data, "common.instructorRoleID");
            usersDB.grantUserRole(username, instructorRoleID);
        }

        LoginPage login = new LoginPage(driver, this.getAppURL());
        login.login(username, password);

        HomePage home = new HomePage(driver, this.getAppURL());
        home.clickBtn(home.course);
        home.clickBtn(home.newCourse);

        return username;
    }


    @Test(priority = 1)
    public void createNewCourseUsingValidData(ITestContext context, Method method) throws Exception {
        String username = this.loginToCreateCoursePage(null);

        JSONObject tcData = (JSONObject) this.data.get("createCourse");
        NewCoursePage coursePage = new NewCoursePage(driver);

        String courseTitle = coursePage.setRandomCourseTitle();
        coursePage.setInputValue(coursePage.duration, (String) tcData.get("duration"));
        coursePage.setInputValue(coursePage.categoryName, (String) tcData.get("categoryName"));
        coursePage.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        coursePage.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        coursePage.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        coursePage.setFutureDateForExpectedFinishedDate();
        coursePage.setInputValue(coursePage.description, (String) tcData.get("description"));
        coursePage.clickBtn(coursePage.btnCreate);

        details = new RequestDetailsPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 3, 500);
        wait.until(ExpectedConditions.visibilityOf(details.requestTitleHeader));
        Assert.assertTrue(details.requestTitleHeader.isDisplayed(), "Request Details isn't displayed");

        context.setAttribute("username", username);
        context.setAttribute("courseTitle", courseTitle);
    }

    @Test(priority = 2, dependsOnMethods = {"createNewCourseUsingValidData"})
    public void validateDuplicateCourseWithSameInstructor(ITestContext context, Method method) throws Exception {
        String username = (String) context.getAttribute("username");
        String courseTitle = (String) context.getAttribute("courseTitle");

        this.loginToCreateCoursePage(username);

        JSONObject tcData = (JSONObject) this.data.get("validateDuplicateCourseWithSameInstructor");
        NewCoursePage coursePage = new NewCoursePage(driver);
        coursePage.setInputValue(coursePage.courseTitle, courseTitle);
        coursePage.setInputValue(coursePage.duration, (String) tcData.get("duration"));
        coursePage.setInputValue(coursePage.categoryName, (String) tcData.get("categoryName"));
        coursePage.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        coursePage.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        coursePage.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        coursePage.setFutureDateForExpectedFinishedDate();
        coursePage.setInputValue(coursePage.description, (String) tcData.get("description"));
        coursePage.clickBtn(coursePage.btnCreate);

        this.assertAlertError((String) tcData.get("expectedErrMsg"));
        takeScreenshot(driver, "CourseTitleNotUnique");
    }

    @Test(priority = 3, dependsOnMethods = "createNewCourseUsingValidData")
    public void validateMaxLimitOfCreateNewCourses() throws Exception {
        this.loginToCreateCoursePage(null);

        String maxLimitConfigID = JSONResReader.readProperty(this.data, "common.maxLimitOfNewCourseID");
        Integer maxLimit = Integer.parseInt(maxLimitConfigID);

        NewCoursePage coursePage = new NewCoursePage(driver);
        JSONObject tcData = (JSONObject) this.data.get("tc-validateMaxLimitOfCreateNewCourses");
        for(int i=0; i<maxLimit; i++){
            coursePage.setRandomCourseTitle();
            coursePage.setInputValue(coursePage.duration, (String) tcData.get("duration"));
            coursePage.setInputValue(coursePage.categoryName, (String) tcData.get("categoryName"));
            coursePage.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
            coursePage.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
            coursePage.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
            coursePage.setFutureDateForExpectedFinishedDate();

            coursePage.clickBtn(coursePage.btnCreate);

            details = new RequestDetailsPage(driver);
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.visibilityOf(details.requestTitleHeader));
            Assert.assertTrue(details.requestTitleHeader.isDisplayed(), "Request Details isn't displayed");

            HomePage home = new HomePage(driver, this.getAppURL());
            wait.until(ExpectedConditions.visibilityOf(home.course));

            coursePage.clickBtn(home.course);
            coursePage.clickBtn(home.newCourse);
        }

        coursePage.setRandomCourseTitle();
        coursePage.setInputValue(coursePage.duration, (String) tcData.get("duration"));
        coursePage.setInputValue(coursePage.categoryName, (String) tcData.get("categoryName"));
        coursePage.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        coursePage.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        coursePage.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        coursePage.setFutureDateForExpectedFinishedDate();

        coursePage.clickBtn(coursePage.btnCreate);
        this.assertAlertError((String) tcData.get("expectedAlertErrMsg"));
    }

    @Test(priority = 4)
    public void createNewCourseUsingInValidData() throws Exception {
        String username = JSONResReader.readProperty(this.data, "instructorUser.username");
        this.loginToCreateCoursePage(username);

        JSONArray tcDataArray = (JSONArray) this.data.get("CreateCourseWithInvalidData");
        for (int i = 0; i < tcDataArray.size(); i++) {
            JSONObject courseData = (JSONObject) tcDataArray.get(i);

            NewCoursePage coursePage = new NewCoursePage(driver);

            coursePage.setInputValue(coursePage.courseTitle, (String) courseData.get("courseTitle"));
            coursePage.setInputValue(coursePage.duration, (String) courseData.get("duration"));
            coursePage.setInputValue(coursePage.categoryName, (String) courseData.get("categoryName"));
            coursePage.selectOption(coursePage.courseType, (String) courseData.get("courseType"));
            coursePage.selectOption(coursePage.courseLevel, (String) courseData.get("courseLevel"));
            coursePage.selectOption(coursePage.courseLanguage, (String) courseData.get("courseLanguage"));
            coursePage.setInputValue(coursePage.startDate, (String) courseData.get("startDate"));
            coursePage.setInputValue(coursePage.description, (String) courseData.get("description"));
            coursePage.clickBtn(coursePage.btnCreate);

            String errorMsgIDs = (String) courseData.get("errorMsgIds");
            String[] errorMsgIDAry = errorMsgIDs.split(",");

            for (String errorMsgID : errorMsgIDAry)
                this.assertElementErrorMsg(errorMsgID);

            takeScreenshot(driver, "newCourse");
        }
    }

    @Test(priority = 5)
    public void validateMandatoryField() throws Exception {
        String username = JSONResReader.readProperty(this.data, "instructorUser.username");
        this.loginToCreateCoursePage(username);

        NewCoursePage coursePage = new NewCoursePage(driver);
        coursePage.clickBtn(coursePage.btnCreate);

        JSONObject mandatoryData = (JSONObject) this.data.get("validateMandatoryField");
        JSONArray errorMsgIDs = (JSONArray) mandatoryData.get("errorMsgIds");

        for (Object errorMsgID : errorMsgIDs)
            this.assertElementErrorMsg((String) errorMsgID);
        takeScreenshot(driver, "Mandatoryfield");
    }

    @Test(priority = 6)
    public void validateDurationFieldWithDecimalValue() throws Exception {
        String username = JSONResReader.readProperty(this.data, "instructorUser.username");
        this.loginToCreateCoursePage(username);

        JSONObject tcData = (JSONObject) this.data.get("validateDurationField");
        NewCoursePage coursePage = new NewCoursePage(driver);

        coursePage.setRandomCourseTitle();
        coursePage.setInputValue(coursePage.duration, (String) tcData.get("duration"));
        coursePage.setInputValue(coursePage.categoryName, (String) tcData.get("categoryName"));
        coursePage.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        coursePage.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        coursePage.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        coursePage.setInputValue(coursePage.description, (String) tcData.get("description"));
        coursePage.setFutureDateForExpectedFinishedDate();
        coursePage.clickBtn(coursePage.btnCreate);

        String errorMsgID = (String) tcData.get("errorMsgId");
        this.assertElementErrorMsg(errorMsgID);
    }
}
