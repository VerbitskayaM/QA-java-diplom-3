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
import pageobjects.RegisterPage;

import java.util.UUID;

import static clients.WebDriverSettings.getWebDriver;
import static org.hamcrest.Matchers.containsString;

@DisplayName("Регистрация пользователя")
@RunWith(Parameterized.class)
public class RegisterPageTests {
    private WebDriver webDriver;
    private final String browserName;
    private RegisterPage registerPage;
    private String email, name, password;

    @Parameterized.Parameters(name="Browser {0}")
    public static Object[][] initParams() {
        return new Object[][] {
                {"chrome"},
                {"firefox"}
        };
    }
    public RegisterPageTests(String browserName) {
        this.browserName = browserName;
    }
    @Before
    @Step("Запуск браузера, подготовка тестовых данных")
    public void setUp() {
        webDriver = getWebDriver(browserName);
        webDriver.get(ApiUrls.URL_REGISTER_PAGE);
        registerPage = new RegisterPage(webDriver);

        email = "email_" + UUID.randomUUID() + "@gmail.com";
        name = "name";
        password = "pass_" + UUID.randomUUID();

        Allure.addAttachment("Имя", name);
        Allure.addAttachment("Email", email);
        Allure.addAttachment("Пароль", password);

        Allure.parameter("Браузер", browserName);
    }

    @After
    @Step("Закрытие браузера и очистка данных")
    public void tearDown() {
        webDriver.quit();
        new ApiClient().deleteTestUser(email, password);
    }

    @Test
    @DisplayName("Успешная регистрация")
    public void registerNewUserIsSuccess() {
        registerPage.setEmail(email)
                .setName(name)
                .setPassword(password)
                .clickRegisterButton()
                .waitFormSubmitted("Вход");

        checkFormReload();
    }

    @Test
    @DisplayName("Регистрация с коротким паролем")
    public void registerNewUserLowPasswordIsFailed() {
        registerPage.setEmail(email)
                .setName(name)
                .setPassword(password.substring(0, 3))
                .clickRegisterButton()
                .waitErrorIsVisible();

        checkErrorMessage();
    }

    @Step("Проверка перезагрузки формы регистрации")
    private void checkFormReload() {
        MatcherAssert.assertThat(
                "Форма регистрации не перезагрузилась",
                webDriver.getCurrentUrl(),
                containsString("/login")
        );
    }

    @Step("Проверка появления сообщения об ошибке")
    private void checkErrorMessage() {
        Assert.assertEquals(
                "Некорректное сообщение об ошибке",
                registerPage.getErrorMessage(),
                "Некорректный пароль");
    }
}
