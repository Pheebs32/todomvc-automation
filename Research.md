## Research into parallel testing with selenium

Selenium cannot automatically test the same test over multiple
websites/ frameworks.
<br>I am doing some research into the issue to see if 
there is a way of solving this.
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
