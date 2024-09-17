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
//        chromeOptions.addArguments("--headless=new");
//        chromeOptions.addArguments("--no-sandbox");
//        chromeOptions.addArguments("--disable-dev-shm-usage");
//        chromeOptions.addArguments("--disable-gpu");
//        chromeOptions.addArguments("--disable-software-rasterizer");
//        chromeOptions.addArguments("--no-first-run");
//        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.addArguments("--headless=new");
//        firefoxOptions.addArguments("--no-sandbox");
//        firefoxOptions.addArguments("--disable-dev-shm-usage");
//        firefoxOptions.addArguments("--disable-gpu");
//        firefoxOptions.addArguments("--disable-software-rasterizer");
//        firefoxOptions.addArguments("--no-first-run");
////        firefoxOptions.addArguments("--remote-allow-origins=*");
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
