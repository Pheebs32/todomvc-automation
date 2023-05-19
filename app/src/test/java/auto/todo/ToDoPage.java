package auto.todo;
import org.openqa.selenium.*;

public class ToDoPage {
    protected WebDriver driver;

    public ToDoPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo() {
        driver.get("https://todomvc.com/examples/vanillajs/");
    }

    public void addItem(String itemItem) {
        WebElement todoBox = driver.findElement(By.cssSelector(".new-todo"));
        todoBox.sendKeys(itemItem);
        todoBox.sendKeys(Keys.ENTER);
    }

    public String get1stItemText() {
        return driver.findElement(By.cssSelector(".view > label")).getText();
    }
}
