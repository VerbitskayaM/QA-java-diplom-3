package tests;

import clients.ApiClient;
import clients.ApiUrls;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pageobjects.AuthPage;
import pageobjects.ForgotPasswordPage;
import pageobjects.MainPage;
import pageobjects.RegisterPage;

import java.util.UUID;

import static clients.WebDriverSettings.getWebDriver;

@DisplayName("Авторизация пользователя")
@RunWith(Parameterized.class)
public class AuthTests {
    private WebDriver webDriver;
    private final String browserName;
    private AuthPage authPage;
    private MainPage mainPage;
    private RegisterPage registerPage;
    private ForgotPasswordPage forgotPasswordPage;
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
    public AuthTests(String browserName) {
        this.browserName = browserName;
    }

    @Before
    @Step("Запуск браузера, подготовка тестовых данных")
    public void setUp() {
        webDriver = getWebDriver(browserName);
        webDriver.get(ApiUrls.URL_MAIN_PAGE);

        authPage = new AuthPage(webDriver);
        mainPage = new MainPage(webDriver);
        registerPage = new RegisterPage(webDriver);
        forgotPasswordPage = new ForgotPasswordPage(webDriver);

        String name = "name";
        email = "email_" + UUID.randomUUID() + "@gmail.com";
        password = "pass_" + UUID.randomUUID();

        Allure.addAttachment("Имя", name);
        Allure.addAttachment("Email", email);
        Allure.addAttachment("Пароль", password);

        apiClient = new ApiClient();
        apiClient.createUser(name, email,password);
    }
    @After
    @Step("Закрытие браузера и очистка данных")
    public void tearDown() {
        webDriver.quit();
        apiClient.deleteTestUser(email, password);
    }

    @Step("Выполняем авторизацию")
    private void authUser() {
        authPage.setEmail(email)
                .setPassword(password)
                .clickAuthButton()
                .waitFormSubmitted();
    }
    @Test
    @DisplayName("Вход по кнопке 'Войти в аккаунт' на главной")
    public void authFromMainIsSuccess() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickAuthButton();
        authPage.waitAuthFormVisible();

        authUser();

        Assert.assertEquals(
                "Ожидаемая надпись на кнопке: 'Оформить заказ'",
                mainPage.getBasketButtonText(),
                "Оформить заказ");
    }
    @Test
    @DisplayName("Вход через кнопку 'Личный кабинет'")
    public void authFromLinkToProfileIsSuccess() {
        Allure.parameter("Браузер", browserName);

        mainPage.clickLinkToProfile();
        authPage.waitAuthFormVisible();

        authUser();

        Assert.assertEquals(
                "Ожидаемая надпись на кнопке: 'Оформить заказ'",
                mainPage.getBasketButtonText(),
                "Оформить заказ");
    }
    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    public void authLinkFromRegFormIsSuccess() {
        Allure.parameter("Браузер", browserName);

        webDriver.get(ApiUrls.URL_REGISTER_PAGE);

        registerPage.clickAuthLink();
        authPage.waitAuthFormVisible();

        authUser();

        Assert.assertEquals(
                "Ожидаемая надпись на кнопке: 'Оформить заказ'",
                mainPage.getBasketButtonText(),
                "Оформить заказ");
    }
    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    public void authLinkFromForgotPasswordFormIsSuccess() {
        Allure.parameter("Браузер", browserName);

        webDriver.get(ApiUrls.URL_FORGOT_PASSWORD_PAGE);

        forgotPasswordPage.clickAuthLink();
        authPage.waitAuthFormVisible();

        authUser();

        Assert.assertEquals(
                "Ожидаемая надпись на кнопке: 'Оформить заказ'",
                mainPage.getBasketButtonText(),
                "Оформить заказ");
    }
}
