package auto.todo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.security.auth.Subject;

public class AppTest {
    private static FirefoxDriver driver;

    @BeforeAll
    static void launchBrowser() {
        driver = new FirefoxDriver();
    }

    @AfterAll
    static void closeBrowser() {
        driver.quit();
    }
}
