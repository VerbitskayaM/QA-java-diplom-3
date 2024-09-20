package clients;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverSettings {
    public static WebDriver getWebDriver(String browserName) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--kiosk");
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        switch (browserName.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
                return new ChromeDriver(chromeOptions);

            case "firefox":
                System.setProperty("webdriver.firefox.driver", "src/test/resources/geckodriver");
                return new FirefoxDriver(firefoxOptions);

            default:
                throw new RuntimeException("Wrong browser name, or wrong browser driver selected");
        }
    }
}
