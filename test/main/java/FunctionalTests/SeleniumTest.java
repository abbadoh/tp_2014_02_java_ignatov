package FunctionalTests;

import junit.framework.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import templater.PageGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gumo on 19/03/14.
 */
public class SeleniumTest {
    @Test
    public void SuccessfulAuthTest() {
        WebDriver driver = new FirefoxDriver();
        String url = "http://localhost:8080/authform";
        String username = "test";
        String password = "test";
        driver.get(url);

        WebElement element = driver.findElement(By.name("login"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element.submit();
        Assert.assertTrue(driver.getTitle().equals("userId"));
        driver.close();
    }

    @Test
    public void UnsuccessfulAuthTest() throws InterruptedException {
        WebDriver driver = new FirefoxDriver();
        String url = "http://localhost:8080/authform";
        String username = "test";
        String password = "false";
        driver.get(url);

        WebElement element = driver.findElement(By.name("login"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element.submit();

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("userState", "wait for authorization");
        pageVariables.put("refreshPeriod", "1000");
        pageVariables.put("lock",true);

//       Assert.assertTrue(driver.toString().equals(PageGenerator.getPage("authform.tml", pageVariables)));
        System.out.println(driver.getPageSource());
//        System.out.println(driver.findElement(By.id("1"                 §   )));
//        Assert.assertTrue(driver.findElement(By.className("titile")).equals("wait for authorization"));
        driver.close();
    }
}
