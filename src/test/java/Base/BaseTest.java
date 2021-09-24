package Base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import com.thoughtworks.gauge.ExecutionContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait webDriverWait;
    protected static Logger logger = LogManager.getLogger(BaseTest.class);

    String browser = System.getenv("browser");

    @BeforeScenario
    public void setUp(ExecutionContext executionContext){

        if(browser.equals("chrome")){
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("incognito");
            System.setProperty("webdriver.chrome.driver", "webDriver/chromedriver");
            driver = new ChromeDriver(chromeOptions);
            driver.manage().window().maximize();
        }
        if (browser.equals("firefox")) {
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("-private");

            System.setProperty("webdriver.gecko.driver", "webDriver/geckodriver");
            driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();
        }
        else if (browser.equals("IE")) {

            System.setProperty("webdriver.ie.driver", "drivers//IEDriverServer.exe");
            driver = new InternetExplorerDriver();
            driver.manage().window().maximize();
        }
        webDriverWait = new WebDriverWait(driver, 45, 150);
    }

    @AfterScenario
    public void tearDown(){

        driver.quit();

    }

}