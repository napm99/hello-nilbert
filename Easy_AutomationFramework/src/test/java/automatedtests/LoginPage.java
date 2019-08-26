package automatedtests;

import automatedevents.WebDriverEvents;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginPage {

    /**
     * Test Case 1: The user access the login page.
     *
     * @param url, Username, UserPassword
     */
    @Test(priority = 1)
    @Parameters({"url", "UserName", "UserPassword"})
    public void accesLoginPage(String url, String UserName, String UserPassword) {
        WebDriverEvents browser = new WebDriverEvents();
        try {
            browser.setBrowser("chrome")
                    .go(url)
                    .fill("username", UserName)
                    .fill("userpassword", UserPassword)
                    .click("submitButton")
                    .validateURL("profilename");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            browser.quitBrowser();
        }

    }

    /**
     * Test Case 1: The user access the login page.
     *
     * @param url
     */
    @Test(priority = 2)
    @Parameters({"url", "UserName", "UserPassword"})
    public void forgotPassword(String url) {
        WebDriverEvents browser = new WebDriverEvents();
        try {
            browser.setBrowser("headless")
                    .go(url)
                    .click("forgotPasswordButton")
                    .validateURL("forgotPasswordPage");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            browser.quitBrowser();
        }

    }
}
