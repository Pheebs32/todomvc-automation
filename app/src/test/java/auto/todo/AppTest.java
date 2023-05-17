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
        driver.get("https://todomvc.com/examples/vanillajs/");
    }

    @Test
    void testCreatesTodo() {
        // Create a new item
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        // Verify count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(1, Integer.parseInt(todoCount.getText()));

        // Verify item in the list
        List<WebElement> todoList = driver.findElements(By.cssSelector(".todo-list li"));
        assertTrue(todoList.stream().anyMatch(el -> "Buy groceries".equals(el.getText())));
    }

    @Test
    public void testEditTodoItem() {
        // Create a new item
        String todoText = "Buy groceries";
        driver.findElement(By.cssSelector(".new-todo")).sendKeys(todoText);
        WebElement search = driver.findElement(By.cssSelector(".new-todo"));
        search.sendKeys(Keys.RETURN);

        // Get the item
        WebElement todoLi = driver.findElements(By.cssSelector(".todo-list li"))
                .stream()
                .filter(el -> "Buy groceries".equals(el.getText()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Test data missing!"));
        // Edit the item
        new Actions(driver).doubleClick(todoLi).perform();

        WebElement todoEditInput = todoLi.findElement(By.cssSelector("input.edit"));
        driver.executeScript("arguments[0].value = ''", todoEditInput);
        todoEditInput.sendKeys("Buy groceries and toiletries");
        todoEditInput.sendKeys(Keys.ENTER);

        // verify changed item in the list
        List<WebElement> todoList = driver.findElements(By.cssSelector(".todo-list li"));
        assertTrue(todoList.stream().anyMatch(el -> "Buy groceries and toiletries".equals(el.getText())));
    }

    @Test
    public void testRemovesTodo() {
        // Create a new item
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        // Get item by name
        WebElement todoLi = driver.findElements(By.cssSelector(".todo-list li"))
                .stream()
                .filter(el -> "Buy groceries".equals(el.getText()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Test data missing!"));

        // Move mouse to the item
        new Actions(driver).moveToElement(todoLi).perform();

        // Remove the item
        WebElement todoDestroyButton = todoLi.findElement(By.cssSelector("button.destroy"));
        todoDestroyButton.click();

        // Verify no item in the list
        List<WebElement> todoList = driver.findElements(By.cssSelector(".todo-list li"));
        assertTrue(todoList.stream().noneMatch(el -> "Buy groceries".equals(el.getText())));
    }

    @Test
    public void testTogglesTodoCompleted() {
        // Create new items
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        todoInput.sendKeys("Buy dog food");
        todoInput.sendKeys(Keys.ENTER);

        // Get the item by name
        WebElement todoLi = driver.findElements(By.cssSelector(".todo-list li"))
                .stream()
                .filter(el -> "Buy groceries".equals(el.getText()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Test data missing!"));

        // Toggle item completed
        WebElement todoToggleCheckbox = todoLi.findElement(By.cssSelector("input.toggle"));
        todoToggleCheckbox.click();

        // Verify count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(1, Integer.parseInt(todoCount.getText()));

        // Show completed items
        WebElement completedLink = driver.findElement(By.cssSelector("a[href='#/completed']"));
        completedLink.click();

        // Verify completed items count
        List<WebElement> completedTodos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(1, completedTodos.size());

        // Show active items
        WebElement activeLink = driver.findElement(By.cssSelector("a[href='#/active']"));
        activeLink.click();

        // Verify active items count
        List<WebElement> activeTodos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(1, activeTodos.size());
    }

    @Test
    void testTogglesAllTodosCompleted() {
        // Create new items
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        todoInput.sendKeys("Buy dog food");
        todoInput.sendKeys(Keys.ENTER);

        // Toggle all items
        WebElement toggleAllCheckbox = driver.findElement(By.className("toggle-all"));
        toggleAllCheckbox.click();

        // Verify count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(0, Integer.parseInt(todoCount.getText()));

        // Show completed items
        WebElement completedLink = driver.findElement(By.cssSelector("a[href='#/completed']"));
        completedLink.click();

        // Verify completed items count
        List<WebElement> completedTodos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(2, completedTodos.size());

        // Show active items
        WebElement activeLink = driver.findElement(By.cssSelector("a[href='#/active']"));
        activeLink.click();

        // Verify active items count
        List<WebElement> activeTodos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(0, activeTodos.size());
    }

    @Test
    void testClearsCompletedTodos() {
        // Create new items
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        todoInput.sendKeys("Buy dog food");
        todoInput.sendKeys(Keys.ENTER);

        // Toggle all items
        WebElement toggleAllCheckbox = driver.findElement(By.className("toggle-all"));
        toggleAllCheckbox.click();

        // Create a new item
        todoInput.sendKeys("Buy toiletries");
        todoInput.sendKeys(Keys.ENTER);

        // Clear completed items
        WebElement clearCompletedButton = driver.findElement(By.className("clear-completed"));
        clearCompletedButton.click();

        // Verify count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(1, Integer.parseInt(todoCount.getText()));

        // Show completed items
        WebElement completedLink = driver.findElement(By.cssSelector("a[href='#/completed']"));
        completedLink.click();

        // Verify completed items count
        List<WebElement> completedTodos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(0, completedTodos.size());

        // Show active items
        WebElement activeLink = driver.findElement(By.cssSelector("a[href='#/active']"));
        activeLink.click();

        // Verify active items count
        List<WebElement> activeTodos = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(1, activeTodos.size());
    }

    @AfterEach
    void closeBrowser() {
        driver.quit();
    }
}
