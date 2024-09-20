package pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthPage {
    private final WebDriver driver;
    private final By header = By.tagName("h1");
    private final By inputs = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class='input__container']//input");
    private final By authButton = By.xpath(".//form[starts-with(@class, 'Auth_form')]/button");
    private final By title = By.xpath(".//main//h2");
    private final By modalOverlay = By.xpath(".//div[starts-with(@class, 'App_App')]/div/div[starts-with(@class, 'Modal_modal_overlay')]");

    public AuthPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Заполнение email")
    public AuthPage setEmail(String email) {
        driver.findElements(inputs).get(0).sendKeys(email);
        return this;
    }

    @Step("Заполнение password")
    public AuthPage setPassword(String password) {
        driver.findElements(inputs).get(1).sendKeys(password);
        return this;
    }

    @Step("Нажатие на кнопку авторизации")
    public AuthPage clickAuthButton() {
        waitButtonIsClickable();
        driver.findElement(authButton).click();
        return this;
    }

    public void waitButtonIsClickable() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.invisibilityOf(driver.findElement(modalOverlay)));
    }

    public void waitFormSubmitted() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOfElementLocated(header));
    }

    public void waitAuthFormVisible() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.textToBe(title, "Вход"));
    }
}
