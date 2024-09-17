package pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private final WebDriver webDriver;
    private final By inputs = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class='input__container']//input");
    private final By registerButton = By.xpath(".//form[starts-with(@class, 'Auth_form')]/button");
    private final By errorMessage = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class='input__container']//p[starts-with(@class,'input__error')]");
    private final By title = By.xpath(".//main//h2");
    private final By authLink = By.xpath(".//a[starts-with(@class,'Auth_link')]");
    private final By modalOverlay = By.xpath(".//div[starts-with(@class, 'App_App')]/div[starts-with(@class, 'Modal_modal')]");

    public RegisterPage(WebDriver driver) {
        webDriver = driver;
    }

    @Step("Заполнение имени")
    public RegisterPage setName(String name) {
        webDriver.findElements(inputs).get(0).sendKeys(name);
        return this;
    }

    @Step("Заполнение email")
    public RegisterPage setEmail(String email) {
        webDriver.findElements(inputs).get(1).sendKeys(email);
        return this;
    }

    @Step("Заполнение password")
    public RegisterPage setPassword(String password) {
        webDriver.findElements(inputs).get(2).sendKeys(password);
        return this;
    }

    @Step("Нажатие на кнопку регистрации")
    public RegisterPage clickRegisterButton() {
        waitButtonIsClickable();
        webDriver.findElement(registerButton).click();
        return this;
    }

    private void waitButtonIsClickable() {
        new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(ExpectedConditions.invisibilityOf(webDriver.findElement(modalOverlay)));
    }

    public void waitFormSubmitted(String expectedTitle) {
        new WebDriverWait(webDriver, Duration.ofSeconds(5))
                .until(ExpectedConditions.textToBe(title, expectedTitle));
    }

    public void waitErrorIsVisible() {
        new WebDriverWait(webDriver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(webDriver.findElement(errorMessage)));
    }

    public String getErrorMessage() {
        return webDriver.findElement(errorMessage).getText();
    }

    public void clickAuthLink() {
        waitButtonIsClickable();
        webDriver.findElement(authLink).click();
    }
}
