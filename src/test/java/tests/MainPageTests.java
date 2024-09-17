package tests;

import clients.ApiClient;
import clients.ApiUrls;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pageobjects.MainPage;

import static clients.WebDriverSettings.getWebDriver;

@DisplayName("Проверки конструктора (главной страницы)")
@RunWith(Parameterized.class)
public class MainPageTests {
    private WebDriver driver;
    private final String browserName;
    private MainPage mainPage;
    @Parameterized.Parameters(name="Browser {0}")
    public static Object[][] initParams() {
        return new Object[][] {
                {"chrome"},
                {"firefox"}
        };
    }
    public MainPageTests(String browserName) {
        this.browserName = browserName;
    }
    @Before
    @Step("Запускаем браузер")
    public void setUp() {
        driver = getWebDriver(browserName);
        driver.get(ApiUrls.URL_MAIN_PAGE);
        mainPage = new MainPage(driver);
        Allure.parameter("Браузер", browserName);
    }
    @After
    @Step("Закрываем браузер")
    public void tearDown() {
        driver.quit();
    }
    @Test
    @DisplayName("Проверка работы вкладки Булки в разделе с ингредиентами")
    public void checkNavBunsIsSuccess() {
        int expectedLocation = mainPage.getIngredientTitleExpectedLocation();

        mainPage.clickToppingsButton();
        mainPage.clickBunsButton();

        Assert.assertEquals(
                "На странице не отображаются булки",
                mainPage.getBunsLocation(),
                expectedLocation);
    }
    @Test
    @DisplayName("Проверка работы вкладки Соусы в разделе с ингредиентами")
    public void checkNavToppingsIsSuccess() {
        int expectedLocation = mainPage.getIngredientTitleExpectedLocation();

        mainPage.clickToppingsButton();

        Assert.assertEquals(
                "На странице не отображаются соусы",
                mainPage.getToppingsLocation(),
                expectedLocation);
    }
    @Test
    @DisplayName("Проверка работы вкладки Начинки в разделе с ингредиентами")
    public void checkNavFillingsIsSuccess() {
        int expectedLocation = mainPage.getIngredientTitleExpectedLocation();

        mainPage.clickFillingsButton();

        Assert.assertEquals(
                "На странице не отображаются начинки",
                mainPage.getFillingsLocation(),
                expectedLocation);
    }
}
