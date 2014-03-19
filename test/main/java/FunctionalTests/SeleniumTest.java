package FunctionalTests;

import junit.framework.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
/**
 * Created by gumo on 19/03/14.
 */
public class SeleniumTest {
    @Test
    public void SuccessfulAuthTest()
    {
        WebDriver driver = new FirefoxDriver();
        String url = "http://localhost:8080/authform";
        String username="test";
        String password="test";
        driver.get(url);

        WebElement element = driver.findElement(By.name("login"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element.submit();
        Assert.assertTrue(driver.getTitle().equals("userId"));
    }
    @Test
    public void UnsuccessfulAuthTest()
    {
        WebDriver driver = new FirefoxDriver();
        String url = "http://localhost:8080/authform";
        String username="test";
        String password="false";
        driver.get(url);

        WebElement element = driver.findElement(By.name("login"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        element.submit();
        Assert.assertTrue(driver.getTitle().equals("log in"));
    }
}
