package tests.course;

import common.CourseFileNames;
import database.UsersDB;
import database.course.CreateCourseDB;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import pages.*;
import pages.request.RequestDetailsPage;
import pages.security.LoginPage;
import pages.security.RegistrationPage;
import tests.TestBase;
import util.JSONResReader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class CreateCourseTC extends TestBase {
    public CreateCourseDB db;
    public UsersDB usersDB;

    public CreateCourseTC() throws SQLException, ClassNotFoundException, IOException, ParseException {
        super();
        db = new CreateCourseDB(this.jsonConfig);
        usersDB = new UsersDB(this.jsonConfig);
    }

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = CourseFileNames.CREATE_COURSE_TEST_DATA;
    }

    private LoginPage login;
    private RequestDetailsPage details;

    private String loginToCreateCoursePage(String username, String password) throws SQLException, ClassNotFoundException, IOException, ParseException, InterruptedException {
        if(username == null && password == null) {
            JSONObject corDBParams = (JSONObject) JSONResReader.readJSONResource(CourseFileNames.COURSE_DATABASE_PARAMS);

            String maxLimitConfigID = JSONResReader.readProperty(corDBParams, "systemConfigs.maxLimitOfNewCourseID");
            String instructorRoleID = JSONResReader.readProperty(corDBParams, "roles.instructorID");
            username = db.selectUsernameWithCanCreateCourse(instructorRoleID, maxLimitConfigID);
            password = JSONResReader.readProperty(corDBParams, "users.password");
            if (username == null) {
                RegistrationPage registrationPage = new RegistrationPage(driver, this.getAppURL());
                username = registrationPage.registerNewUser("instructor_", password);

                UsersDB usersDB = new UsersDB(this.jsonConfig);
                usersDB.grantUserRole(username, instructorRoleID);
            }
        }

        login = new LoginPage(driver, this.getAppURL());
        login.login(username, password);

        HomePage home = new HomePage(driver, this.getAppURL());
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOf(home.course));

        PageBase.clickButton(home.course);
        PageBase.clickButton(home.newCourse);
        return username;
    }

    private String loginToCreateCoursePage() throws SQLException, ClassNotFoundException, IOException, ParseException, InterruptedException {
        return loginToCreateCoursePage(null, null);
    }

    @Test(priority = 1)
    public void createNewCourseUsingValidData(ITestContext context, Method method) throws Exception {
        String username = this.loginToCreateCoursePage();

        JSONObject tcData = (JSONObject) this.data.get("createCourse");
        NewCoursePage coursePage = new NewCoursePage(driver);

        String courseTitle = coursePage.setRandomCourseTitle();
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        coursePage.setFutureDateForExpectedFinishedDate();
        PageBase.setText(coursePage.description, (String) tcData.get("description"));
        PageBase.clickButton(coursePage.btnCreate);

        details = new RequestDetailsPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOf(details.requestTitleHeader));
        Assert.assertTrue(details.requestTitleHeader.isDisplayed(), "Request Details isn't displayed");

        context.setAttribute("username", username);
        context.setAttribute("courseTitle", courseTitle);
    }

    @Test(priority = 2, dependsOnMethods = {"createNewCourseUsingValidData"})
    public void validateDuplicateCourseWithSameInstructor(ITestContext context, Method method) throws Exception {
        String username = (String) context.getAttribute("username");
        String courseTitle = (String) context.getAttribute("courseTitle");

        JSONObject corDBParams = (JSONObject) JSONResReader.readJSONResource(CourseFileNames.COURSE_DATABASE_PARAMS);
        String password = JSONResReader.readProperty(corDBParams,"users.password");
        this.loginToCreateCoursePage(username, password);

        JSONObject tcData = (JSONObject) this.data.get("validateDuplicateCourseWithSameInstructor");
        NewCoursePage coursePage = new NewCoursePage(driver);
        PageBase.setText(coursePage.courseTitle, courseTitle);
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        coursePage.setFutureDateForExpectedFinishedDate();
        PageBase.setText(coursePage.description, (String) tcData.get("description"));
        PageBase.clickButton(coursePage.btnCreate);

        this.assertAlertError((String) tcData.get("expectedErrMsg"));
        takeScreenshot(driver, "CourseTitleNotUnique");
    }

    @Test(priority = 3, dependsOnMethods = "createNewCourseUsingValidData")
    public void validateMaxLimitOfCreateNewCourses() throws Exception {
        String username = this.loginToCreateCoursePage();

        JSONObject corDBParams = (JSONObject) JSONResReader.readJSONResource(CourseFileNames.COURSE_DATABASE_PARAMS);
        String maxLimitConfigID = JSONResReader.readProperty(corDBParams, "systemConfigs.maxLimitOfNewCourseID");

        Integer maxLimit = db.getMaxLimitOfNewCourseConfiguration(maxLimitConfigID);

        Integer userID = usersDB.getUserID(username);
        Integer count = db.countOfUnCompletedCourses(userID.toString());

        Integer diff = maxLimit - count;
        NewCoursePage coursePage = new NewCoursePage(driver);

        JSONObject tcData = (JSONObject) this.data.get("tc-validateMaxLimitOfCreateNewCourses");
        for(int i=0; i<diff; i++){
            coursePage.setRandomCourseTitle();
            PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
            PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
            PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
            PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
            PageBase.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
            coursePage.setFutureDateForExpectedFinishedDate();

            PageBase.clickButton(coursePage.btnCreate);

            details = new RequestDetailsPage(driver);
            WebDriverWait wait = new WebDriverWait(driver, 2);
            wait.until(ExpectedConditions.visibilityOf(details.requestTitleHeader));
            Assert.assertTrue(details.requestTitleHeader.isDisplayed(), "Request Details isn't displayed");

            HomePage home = new HomePage(driver, this.getAppURL());
            wait.until(ExpectedConditions.visibilityOf(home.course));

            PageBase.clickButton(home.course);
            PageBase.clickButton(home.newCourse);
        }

        coursePage.setRandomCourseTitle();
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        coursePage.setFutureDateForExpectedFinishedDate();

        PageBase.clickButton(coursePage.btnCreate);

        this.assertAlertError((String) tcData.get("expectedAlertErrMsg"));
    }

    @Test(priority = 4)
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
            PageBase.selectOption(coursePage.courseLanguage, (String) courseData.get("courseLanguage"));
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

    @Test(priority = 5)
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

    @Test(priority = 6)
    public void validateDurationFieldWithDecimalValue() throws Exception {
        this.loginToCreateCoursePage();

        JSONObject tcData = (JSONObject) this.data.get("validateDurationField");
        NewCoursePage coursePage = new NewCoursePage(driver);

        coursePage.setRandomCourseTitle();
        PageBase.setText(coursePage.duration, (String) tcData.get("duration"));
        PageBase.setText(coursePage.categoryName, (String) tcData.get("categoryName"));
        PageBase.selectOption(coursePage.courseType, (String) tcData.get("courseType"));
        PageBase.selectOption(coursePage.courseLevel, (String) tcData.get("courseLevel"));
        PageBase.selectOption(coursePage.courseLanguage, (String) tcData.get("courseLanguage"));
        PageBase.setText(coursePage.description, (String) tcData.get("description"));
        coursePage.setFutureDateForExpectedFinishedDate();
        PageBase.clickButton(coursePage.btnCreate);

        String errorMsgID = (String) tcData.get("errorMsgId");
        this.assertElementErrorMsg(errorMsgID);
    }
}
