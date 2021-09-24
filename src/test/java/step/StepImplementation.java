package step;

import Base.BaseMethods;
import com.thoughtworks.gauge.Step;
import helper.ElementHelper;
import helper.StoreHelper;
import model.ElementInfo;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class StepImplementation extends BaseMethods {


    @Step("<second> second wait")
    public void waitBySeconds(int seconds) {
        waitByMilliSeconds(seconds * 1000);
    }


    @Step("Go to <url>")
    public void goToUrl(String url) {

        driver.get(url);
        logger.info(url + " going to.");

    }

    @Step("Wait for <key> and click")
    public void checkElementVisibiltyAndClick(String key) {
        isElementVisible(key, 10);
        isElementClickable(key, 10);
        clickElement(key);
    }

    @Step("Hover to <key>")
    public void hoverStep(String key) {
        isElementVisible(key, 5);
        hoverElement(key);
    }

    @Step("Is <key> element Visible ? <timeout>")
    public boolean isElementVisible(String key, int timeout) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.visibilityOfElementLocated(ElementHelper.getElementInfoToBy(elementInfo)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("İs <key> element Clickable ?, <timeout>")

    public boolean isElementClickable(String key, int timeout) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(ExpectedConditions.elementToBeClickable(ElementHelper.getElementInfoToBy(elementInfo)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Step("Write <text> to the <key> and clear area")
    public void sendKeys(String text, String key) {

        clearAndSendKey(text, key);
        logger.info(text + " written to " + key);
    }

    @Step("Check page title text <tabText>")
    public final void assertPage(String expectedPageTitle) {

        String titleText = driver.getTitle();
        System.out.println("Title " + titleText);
        if (expectedPageTitle.contains(titleText)) {
            System.out.println("Title " + titleText);

            Assert.assertEquals("Page title eşleşmiyor", expectedPageTitle, titleText);
        }

    }

    @Step("Logger -> <text>")
    public void loggerInfo(String text) {

        logger.info(text);

    }

    @Step("Javascript ile tıkla <key>")
    public void javaScriptClicker(String key) {
        isElementVisible(key, 5);
        isElementClickable(key, 5);
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = driver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    @Step("Click recaptcha ok button")
    public void clickRecaptcha() {
        WebElement iFrame = driver.findElement(By.tagName("iframe"));
        driver.switchTo().frame(iFrame);
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(By.cssSelector("#recaptcha-anchor > div.recaptcha-checkbox-checkmark"));
        actions.moveToElement(element).click().build().perform();
        driver.switchTo().defaultContent();

    }

// Captcha aşımı için coockieler silindiğinde aştığım projeler oldu fakat burada işlemedi.
// WebDriverWait ile reCaptcha image dogrulamasi olmadan asilabildi ancak stabil calismadigi görüldü
    @Step("Check recaptcha checbox")
    public void checkRecaptchaCheckbox(){
        new WebDriverWait(driver, 20).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.cssSelector("iframe[title='reCAPTCHA']")));
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[class='recaptcha-checkbox-border']"))).click();
        driver.switchTo().defaultContent();
        waitBySeconds(4);

    }

    @Step("<key> if element is not exist go fail <message>")
    public void assertTrue(String key, String message) {
        Assert.assertTrue(message, isElementVisible(key, 10));

    }

    @Step("<key> if element is exist go fail <message>")
    public void assertFalse(String key, String message) {
        Assert.assertFalse(message, isElementVisible(key, 10));
    }

    @Step("Is captcha on the page control")
    public void shouldHaveCaptchaField() {
        Assert.assertTrue("Captcha field not found", driver.findElements(By.id("recaptcha")).size() > 0);
    }
    @Step("Scroll to <key>")
    public WebElement scrollTo(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = driver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        isElementVisible(key,10);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        return element;
    }
    @Step("Scroll to by <key>")
    public WebElement scrollToBy(By by) {
        WebElement element = driver.findElement(by);
        waitPresenceOfElementLocatedBy(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        return element;
    }
    public void type(By by, String text, boolean clear) {
        WebElement element = scrollToBy(by);
        if (clear) {
            element.clear();
        }
        element.sendKeys(text);
    }
    @Step("Upload <cv> the CV")
    public void uploadCV(String fileName) {
        File file = new File("src/main/resources/cvdoc/" + fileName);
        type(By.id("jobFile"), file.getAbsolutePath(), false);
    }
}
