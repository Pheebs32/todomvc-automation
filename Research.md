## Research into parallel testing with selenium

Selenium cannot automatically test the same test over multiple
websites/ frameworks.
<br>I am doing some research into the issue to see if 
there is a way of solving this.

Parallel testing is a process where multiple tests are executed 
simultaneously/in parallel in different thread processes. With 
respect to Selenium, it allows you to execute multiple tests on 
different browsers, devices, environments in parallel and at the 
same time, instead of running it sequentially.

Reference - https://www.youtube.com/watch?v=M93T8T69akw

#### In AppTest
```java:
public class BaseTest {
    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
    public static String remote_url = "https://todomvc.com/examples/vanillajs/";
    public Capabilities capabilities;
 
    @Parameters({"browser"})
    @BeforeMethod
    public void setDriver(String browser) throws MalformedURLException {
        if(browser.equals("firefox")) {
            capabilities = new FirefoxOptions();
        } else if (browser.equals("chrome")) {
            capabilities = new ChromeOptions();
        } else if (browser.equals("edge")) {
            capabilities = new EdgeOptions();
        }
```

#### Make .XML file?
```.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">

<suite name="Suite">
  <test thread-count="5" name="Chrome Test">
    <parameter name ="browser" value="chrome"/>
    <classes>
      <class name="org.example.Selenium4ParallelDemo"/>
    </classes>
  </test> <!-- Test -->

   <test thread-count="5" name="Firefox Test">
    <parameter name ="browser" value="firefox"/>
    <classes>
      <class name="org.example.Selenium4ParallelDemo"/>
    </classes>
  </test> <!-- Test -->

   <test thread-count="5" name="Edge Test">
    <parameter name ="browser" value="edge"/>
    <classes>
      <class name="org.example.Selenium4ParallelDemo"/>
    </classes>
  </test>
</suite> 
```
#### Run the tests from .XML file

#### How TestApp.java should look like?

```java
package paralleltesting;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromDriver;
import org.testng.annotations.Test;

public class parallelTest1 {
    WebDriver driver;

    @Test
    void logoTest() {
        System.setProperty("webdriver.chrome.driver", "/applications/Google Chrome.app");
        driver = new ChromeDriver();

        driver.get("https://todomvc.com/examples/vanillajs/");
        WebElement logo = driver.findElement(By.cssSelector("h1"));

        Assert.assertTrue(logo.isDisplayed());
    }

    @AfterTest
    void tearDown() {
        driver.quit();
    }
}
```

I don't understand fully know how to implement this yet but hopefully with more 
research and time this can be achieved. Whilst this may not be implemented into
this package of tests I will try to incorporate it in the future.
