package tests;

import clients.ApiClient;
import clients.ApiUrls;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pageobjects.AuthPage;
import pageobjects.MainPage;
import pageobjects.ProfilePage;

import java.util.UUID;

import static clients.WebDriverSettings.getWebDriver;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Проверки личного кабинета пользователя")
@RunWith(Parameterized.class)
public class ProfilePageTests {
    private WebDriver driver;
    private final String browserName;
    private AuthPage authPage;
    private MainPage mainPage;
    private ProfilePage profilePage;
    private String email;
    private String password;
    private ApiClient apiClient;

    @Parameterized.Parameters(name="Browser {0}")
    public static Object[][] initParams() {
        return new Object[][] {
                {"chrome"},
                {"firefox"}
        };
    }
    public ProfilePageTests(String browserName) {
        this.browserName = browserName;
    }
    @Before
    @Step("Запуск браузера, подготовка тестовых данных")
    public void setUp() {
        driver = getWebDriver(browserName);
        driver.get(ApiUrls.URL_MAIN_PAGE);

        authPage = new AuthPage(driver);
        mainPage = new MainPage(driver);
        profilePage = new ProfilePage(driver);

        String name = "name";
        email = "email_" + UUID.randomUUID() + "@gmail.com";
        password = "pass_" + UUID.randomUUID();

        Allure.addAttachment("Имя", name);
        Allure.addAttachment("Email", email);
        Allure.addAttachment("Пароль", password);

        apiClient = new ApiClient();
        apiClient.createUser(name, email,password);

        Allure.parameter("Браузер", browserName);
        goToProfile();
    }
    @After
    @Step("Закрываем браузер и чистим данные")
    public void tearDown() {
        driver.quit();
        apiClient.deleteTestUser(email, password);
    }
    @Step("Выполняем авторизацию")
    private void authUser() {
        authPage.setEmail(email)
                .setPassword(password)
                .clickAuthButton()
                .waitFormSubmitted();
    }
    @Step("Переход в личный кабинет")
    private void goToProfile() {
        driver.get(ApiUrls.URL_LOGIN_PAGE);
        authPage.waitAuthFormVisible();

        authUser();

        mainPage.clickLinkToProfile();
        profilePage.waitAuthFormVisible();
    }
    @Test
    @DisplayName("Переход по клику на 'Личный кабинет'")
    public void checkLinkToProfileIsSuccess() {
        MatcherAssert.assertThat(
                "Некорректный URL страницы Личного кабинета",
                driver.getCurrentUrl(),
                containsString("/account/profile")
        );
    }
    @Test
    @DisplayName("Переход из личного кабинета по клику на 'Конструктор'")
    public void checkLinkToConstructorIsSuccess() {
        profilePage.clickLinkToConstructor();
        mainPage.waitHeaderIsVisible();

        MatcherAssert.assertThat(
                "Ожидаемая надпись на кнопке: 'Оформить заказ'",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }
    @Test
    @DisplayName("Переход из личного кабинета по клику на логотип Stellar Burgers")
    public void checkLinkOnLogoIsSuccess() {
        profilePage.clickLinkOnLogo();
        mainPage.waitHeaderIsVisible();

        MatcherAssert.assertThat(
                "Ожидаемая надпись на кнопке: 'Оформить заказ'",
                mainPage.getBasketButtonText(),
                equalTo("Оформить заказ")
        );
    }
    @Test
    @DisplayName("Выход из личного кабинета по клику на кнопку 'Выйти'")
    public void checkLinkLogOutIsSuccess() {
        profilePage.clickLogoutLink();
        authPage.waitAuthFormVisible();

        MatcherAssert.assertThat(
                "Некорректный URL страницы Авторизации",
                driver.getCurrentUrl(),
                containsString("/login")
        );
    }
}
