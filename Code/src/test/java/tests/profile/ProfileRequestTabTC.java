package tests.profile;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.PageBase;
import pages.profile.ProfileRequestTab;
import pages.request.RequestDetailsPage;
import pages.request.RequestInfoTab;
import tests.TestBase;

import java.util.List;

public class ProfileRequestTabTC extends TestBase {

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = "profile/profile-tab-data.json";
    }

    private ProfileRequestTab loginToProfileRequestTab(){
        HomePage home = new HomePage(driver, this.getAppURL());
        home.signOutIfLoggedIn();

        LoginPage login = new LoginPage(driver, this.getAppURL());
        JSONObject loginCredentials = (JSONObject) this.data.get("instructorUser");
        login.login((String) loginCredentials.get("userName"), (String) loginCredentials.get("password"));

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.profileDropDownList));
        PageBase.clickButton(home.profileDropDownList);
        PageBase.clickButton(home.profileAnchor);

        ProfileRequestTab profilePage = new ProfileRequestTab(driver);
        PageBase.clickButton(profilePage.requestsTab);
        return profilePage;
    }


    @Test
    public void loadAllRequests(){
        ProfileRequestTab requestsTab = this.loginToProfileRequestTab();
        Assert.assertTrue(requestsTab.requestsRows.size() > 0, "No Requests appear in Request List");
    }

    @Test
    public void navigateToRequestDetails() throws InterruptedException {
        ProfileRequestTab requestsTab = this.loginToProfileRequestTab();
        Assert.assertTrue(requestsTab.requestsRows.size() > 0, "No Requests appear in Request List");

        WebElement firstRow = requestsTab.requestsRows.get(0);
        List<WebElement> firstRowCells = firstRow.findElements(By.tagName("td"));
        String reqTitle = firstRowCells.get(0).getText();
        String workflow = firstRowCells.get(1).getText();
        String status = firstRowCells.get(2).getText();
        String pendingOn = firstRowCells.get(3).getText();
        PageBase.clickButton(firstRowCells.get(0));

        RequestDetailsPage requestDetailsPage = new RequestDetailsPage(driver);
        Thread.sleep(5000);
        Assert.assertEquals(reqTitle, requestDetailsPage.requestTitleHeader.getText(),
                "Request Title doesn't match the clicked request from the list");

        RequestInfoTab infoTab = new RequestInfoTab(driver);

        Assert.assertEquals(reqTitle, infoTab.reqTitleInfo.getText(),
                "Request Title doesn't match the clicked request from the list");

        Assert.assertEquals(workflow, infoTab.reqWorkflowInfo.getText(),
                "Request Workflow doesn't match the clicked request from the list");

        Assert.assertEquals(status, infoTab.reqStatusInfo.getText(),
                "Request Status doesn't match the clicked request from the list");

        if(pendingOn != null && !pendingOn.trim().isEmpty())
            Assert.assertEquals(pendingOn, infoTab.reqPendingOnInfo.getText(),
                    "Request Pending on doesn't match the clicked request from the list");
    }

    @Test
    public void filterWithRequestTitle() throws InterruptedException {
        ProfileRequestTab requestsTab = this.loginToProfileRequestTab();
        Assert.assertTrue(requestsTab.requestsRows.size() > 0, "No Requests appear in Request List");

        WebElement firstRow = requestsTab.requestsRows.get(0);
        String firstRowReqTitle = firstRow.findElement(By.tagName("td")).getText();
        Assert.assertFalse(firstRowReqTitle.trim().isEmpty(), "Empty Request Title");

        PageBase.setText(requestsTab.reqTitleTxt, firstRowReqTitle);
        PageBase.clickButton(requestsTab.searchBtn);

        Thread.sleep(5000);
        Assert.assertTrue(requestsTab.requestsRows.size() > 0, "No Requests retrieved in the list");

        for(WebElement row: requestsTab.requestsRows)
            Assert.assertTrue(row.findElement(By.tagName("td")).getText().matches(".*(" + firstRowReqTitle + ").*"),
                    "Row has request with title doesn't contains '" + firstRowReqTitle + "'");

    }
}
