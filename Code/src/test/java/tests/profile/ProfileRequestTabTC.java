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
import pages.profile.ProfileRequestTabPage;
import pages.profile.UserProfilePage;
import tests.TestBase;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProfileRequestTabTC extends TestBase {

    @Override
    protected void setJSONFileName() {
        this.FILE_NAME = "profile/profile-tab-data.json";
    }

    private ProfileRequestTabPage loginToProfileRequestTab(){
        LoginPage login = new LoginPage(driver, this.getAppURL());
        JSONObject loginCredentials = (JSONObject) this.data.get("instructorUser");
        login.login((String) loginCredentials.get("userName"), (String) loginCredentials.get("password"));

        HomePage home = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(home.profileDropDownList));
        PageBase.clickButton(home.profileDropDownList);
        PageBase.clickButton(home.profileAnchor);

        ProfileRequestTabPage profilePage = new ProfileRequestTabPage(driver);
        PageBase.clickButton(profilePage.requestsTab);
        return profilePage;
    }


    @Test
    public void loadAllRequests(){
        ProfileRequestTabPage requestsTab = this.loginToProfileRequestTab();
        Assert.assertTrue(requestsTab.requestsRows.size() > 0, "No Requests appear in Request List");
    }

    @Test
    public void filterWithRequestID() throws InterruptedException {
        ProfileRequestTabPage requestsTab = this.loginToProfileRequestTab();
        Assert.assertTrue(requestsTab.requestsRows.size() > 0, "No Requests appear in Request List");

        WebElement firstRow = requestsTab.requestsRows.get(0);
        WebElement firstRowReqTitle = firstRow.findElement(By.tagName("td"));
        String firstRowReqLink = firstRowReqTitle.getAttribute("ng-reflect-router-link");
        Assert.assertNotNull(firstRowReqLink, "Request Link doesn't exist");

        String[] firstRowReqLinkParts = firstRowReqLink.split("/");
        String firstRowReqID = firstRowReqLinkParts[firstRowReqLinkParts.length-1];
        Assert.assertNotNull(firstRowReqID, "Request ID doesn't exist");

        PageBase.setText(requestsTab.reqIDtxtInput, firstRowReqID);
        PageBase.clickButton(requestsTab.searchBtn);

        Thread.sleep(5000);
        Assert.assertEquals(1, requestsTab.requestsRows.size(), "Requests retrieved must be one request only");
    }
}
