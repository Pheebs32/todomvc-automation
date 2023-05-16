package auto.todo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import javax.security.auth.Subject;
import org.openqa.selenium.Keys;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    private static FirefoxDriver driver;

    @BeforeAll
    static void launchBrowser() {
        driver = new FirefoxDriver();
    }

    @Test
    public void testAddTodoItem() {
        // Add a new item
        driver.get("https://todomvc.com/examples/vanillajs/");
        String todoText = "Buy groceries";
        driver.findElement(By.cssSelector(".new-todo")).sendKeys(todoText);
        WebElement search = driver.findElement(By.cssSelector(".new-todo"));
        search.sendKeys(Keys.RETURN);

        // Wait for the item to be added
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(".todo-list li:nth-child(1) label"), todoText));

        // Verify that the item is added correctly
        String addedTodoText = driver.findElement(By.cssSelector(".todo-list li:nth-child(1) label")).getText();
        assertEquals(todoText, addedTodoText);
    }

    @AfterAll
    static void closeBrowser() {
        driver.quit();
    }
}
