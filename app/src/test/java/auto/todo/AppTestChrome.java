package auto.todo;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;

// Testing in Chrome
public class AppTestChrome {
    private static ChromeDriver driver;

    @BeforeEach
    void launchBrowser() {
        driver = new ChromeDriver();
    }

    @Test
    void newItemSingleCharacter()  {
        ToDoPage todoPage = new ToDoPage(driver);
        todoPage.navigateTo();
        todoPage.addItem("Buy Groceries");
        assertEquals("Buy Groceries", todoPage.get1stItemText());
    }

    @AfterEach
    void closeBrowser() {
        driver.quit();
    }
}
