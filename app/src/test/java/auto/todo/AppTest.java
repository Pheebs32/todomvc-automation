package auto.todo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.openqa.selenium.Keys;
import java.net.HttpURLConnection;

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
    void testEditTodoItem() {
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
    void testEditTodoItemAndCancelIt() {
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
        todoEditInput.sendKeys(Keys.ESCAPE);

        // verify changed item in the list
        List<WebElement> todoList = driver.findElements(By.cssSelector(".todo-list li"));
        assertTrue(todoList.stream().anyMatch(el -> "Buy groceries".equals(el.getText())));
    }

    @Test
    void testRemovesTodo() {
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
    void testTogglesTodoCompleted() {
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
    void testTogglesTodoCompletedAndTogglesItOff() {
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
        todoToggleCheckbox.click();

        // Verify count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(2, Integer.parseInt(todoCount.getText()));

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
        assertEquals(2, activeTodos.size());

        // Show all items
        WebElement allLink = driver.findElement(By.cssSelector("a[href='#/"));
        allLink.click();
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

    @Test
    void testStatusBarHidden() {
        // Search for the footer - it should only be created if there is an item in the list
        WebElement search = driver.findElement(By.xpath("//footer/ul"));
        assertFalse(search.isDisplayed());
    }

    @Test
    void testStatusBarCount1() {
        // Create a new item
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        // Verify item count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(1, Integer.parseInt(todoCount.getText()));
    }

    @Test
    void testStatusBarCount2() {
        // Create new items
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("Buy groceries");
        todoInput.sendKeys(Keys.ENTER);

        todoInput.sendKeys("Buy dog food");
        todoInput.sendKeys(Keys.ENTER);

        // Verify item count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(2, Integer.parseInt(todoCount.getText()));
    }

    @Test
    void testCantAddNoValueToList() {
        // Try to create a new item
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys(Keys.ENTER);

        // Search for the footer - it should only be created if there is an item in the list
        WebElement search = driver.findElement(By.xpath("//footer/ul"));
        assertFalse(search.isDisplayed());
    }

    @Test
    void testAddTodoSingleCharacter() {
        //Create new items
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("A");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("5");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("-");
        todoInput.sendKeys(Keys.ENTER);

        // Verify item count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(3, Integer.parseInt(todoCount.getText()));
    }

    @Test
    void testAddTodoAccentedCharacter() {
        //Create new items
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("à, è, ì, ò, ù");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("À, È, Ì, Ò, Ù");
        todoInput.sendKeys(Keys.ENTER);

        // Verify item count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(2, Integer.parseInt(todoCount.getText()));
    }

    @Test
    void testAddTodoEmojis() {
        //Create new items
        WebElement todoInput = driver.findElement(By.className("new-todo"));
        todoInput.sendKeys("\uD83E\uDD79");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("\uD83E\uDD2A");
        todoInput.sendKeys("\uD83E\uDD79");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Today we will be playing Mario Kart! \uD83E\uDD2A");
        todoInput.sendKeys(Keys.ENTER);

        // Verify item count
        WebElement todoCount = driver.findElement(By.cssSelector(".todo-count > strong"));
        assertEquals(3, Integer.parseInt(todoCount.getText()));
    }

    @Test
    void testAllLinksOnHomePage() {
        String homePage = "https://todomvc.com/";
        String url;
        HttpURLConnection huc;
        int respCode;
        int broken = 0;
        int working = 0;
        driver.get(homePage);
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (WebElement link : links) {
            url = link.getAttribute("href");
            //System.out.println(url);
            if (url == null || url.isEmpty()) {
                System.out.println(url + "URL is either not configured for anchor tag or it is empty");
                continue;
            } if (!url.startsWith(homePage)) {
                System.out.println(url + "URL belongs to another domain, skipping it.");
                continue;
            } try {
                huc = (HttpURLConnection) (new URL(url).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();
                respCode = huc.getResponseCode();
                if (respCode >= 400) {
                    System.out.println(url + " is a broken link");
                    broken += 1;
                } else {
                    working += 1;
                    System.out.println(url + " is a valid link");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("There are "+ broken + " links broken on the main page out of " + (working + broken) + "!");
    }

    @AfterEach
    void closeBrowser() {
        driver.quit();
    }
}
