package automatedevents;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class WebDriverEvents {
    private static WebDriverEvents action;
    private WebDriver driver = null;
    private JavascriptExecutor jsExecutor;
    private DesiredCapabilities desireCapabilities;
    private WebDriverWait wait;
    private WebElement element;
    private String actualValue;
    final static Logger logger = Logger.getLogger(WebDriverEvents.class);

    public WebDriverEvents setBrowser(String browserName) {
        try {
            if (browserName.equals("chrome")) {
                WebDriverManager.chromedriver().setup();
                driver.manage().window().maximize();
                wait = new WebDriverWait(driver, 30);
            } else if (browserName.equals("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver.manage().window().maximize();
                wait = new WebDriverWait(driver, 30);
            } else if (browserName.equals("headless")) {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opt = new ChromeOptions();
                opt.setHeadless(true);
                wait = new WebDriverWait(driver, 30);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * The method findElement returns true only if the selector passed by parameter
     * is found
     *
     * @param selector
     * @return boolean
     */

    public boolean findElement(String selector) {
        String findCase = null;
        if (selector.contains("/")) {
            findCase = "XPath";
        } else {
            findCase = "notXPath";
        }
        switch (findCase) {
            case "notXPath":
                if (driver.findElements(By.id(selector)).size() != 0) {
                    driver.findElement(By.id(selector));
                    element = driver.findElement(By.id(selector));
                    return true;
                } else if (driver.findElements(By.cssSelector(selector)).size() != 0) {
                    driver.findElement(By.cssSelector(selector));
                    element = driver.findElement(By.cssSelector(selector));
                    return true;
                }
                break;
            case "XPath":
                if (driver.findElements(By.xpath(selector)).size() != 0) {
                    driver.findElements(By.xpath(selector));
                    element = driver.findElement(By.xpath(selector));
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * This method wait for an element implementing a Predicate until the predictor
     * return true.
     *
     * @param selector
     * @throws TimeoutException
     */

    public WebDriverEvents fluentFindElement(String selector) {
        WebDriverWait waitForElement = new WebDriverWait(driver, 30);
        waitForElement.pollingEvery(Duration.ofSeconds(2));
        List<String> selectors = new ArrayList<>();
        selectors.add(selector);
        try {
            waitForElement.until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    for (String selector : selectors) {
                        boolean isFound = findElement(selector);
                        if (isFound) {
                            return isFound;
                        }
                    }
                    return false;
                }
            });
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return action;
    }

    /**
     * This method opens an URL using the get method from the WebDriver Interface
     *
     * @param url
     */
    public WebDriverEvents go(String url) {
        try {
            driver.get(url);
            logger.info("The URL is valid : " + url);
        } catch (Exception e) {
            logger.info("An error has occurred please verify the logs for more information");
            e.printStackTrace();
        }
        return action;
    }

    /**
     * The click method validates the element is displayed in the DOM and throws an
     * ElementNotPresentException if the element is not in DOM
     *
     * @throws StaleElementReferenceException
     */
    public WebDriverEvents click(String selector) {
        try {
            fluentFindElement(selector);
            if (element != null && element.isDisplayed()) {
                element.click();
            } else {
                logger.info("Element is not present in DOM");
            }
        } catch (StaleElementReferenceException e) {
            e.printStackTrace();
            logger.info("The selector is not present in the DOM: " + selector);
        }
        return action;
    }

    /**
     * The method fill fills a text box with a String and it throws an
     * IllegalArgumentException to indicate that a method has been passed an illegal
     * or inappropriate argument.
     *
     * @param selector
     * @param text
     * @throws IllegalArgumentException
     */
    public WebDriverEvents fill(String selector, String text) {
        try {
            fluentFindElement(selector);
            if (element != null && element.isDisplayed()) {
                element.sendKeys(text);
            } else {
                logger.info("The textbox could not be found");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.info("The textbox could not be found " + selector);
        }
        return action;
    }

    /**
     * The method clear is used to clear the input fields
     *
     * @param selector
     */
    public WebDriverEvents clear(String selector) {
        try {
            fluentFindElement(selector);
            if (element != null && element.isDisplayed()) {
                element.clear();
            } else {
                logger.info("The textbox could not be found");
            }
        } catch (Exception e) {
            logger.info("The textbox could not be found " + selector);
            e.printStackTrace();
        }
        return action;
    }

    /**
     * This method validate if an WebElement is display in the Web and throws a
     * NoSuchElementException if the validation fails
     *
     * @param selector
     */
    public WebDriverEvents validateElementInPage(String selector) {
        try {
            fluentFindElement(selector);
            if (element != null && element.isDisplayed()) {
                element.isEnabled();
            } else {
                logger.info("Element is not present in DOM");
            }
        } catch (Exception e) {
            logger.info("Element is not present in DOM " + selector);
            e.printStackTrace();
        }
        return action;
    }

    /**
     * The method validateURL validates the URL contains the String value passed by
     * the user.
     *
     * @param url
     */
    public WebDriverEvents validateURL(String url) {

        try {
            wait = new WebDriverWait(driver, 30);
            wait.pollingEvery(Duration.ofSeconds(2));
            if (wait.until(ExpectedConditions.urlContains(url))) {
                wait = new WebDriverWait(driver, 15);
                ExpectedConditions.urlContains(url);
            } else {
                logger.info("The String : " + url + " could not be found in the URL");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return action;
    }

    /**
     * This method closes the browser and stops the driver process
     *
     * @throws Exception
     */
    public WebDriverEvents quitBrowser() {
        try {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return action;
    }



}
