package auto.todo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import org.openqa.selenium.Keys;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    private static FirefoxDriver driver;

    @BeforeEach
    void launchBrowser() {
        driver = new FirefoxDriver();
    }

    @Test
    void createsTodo() {
        // create new item
        driver.get("https://todomvc.com/examples/vanillajs/");
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        // verify count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(1, Integer.parseInt(todoCount.getText()));

        // verify item in the list
        List<WebElement> todoList = driver.findElements(By.cssSelector(".todo-list li"));
        assertTrue(todoList.stream().anyMatch(el -> "Buy groceries".equals(el.getText())));
    }

    @AfterEach
    void closeBrowser() {
        driver.quit();
    }
}
