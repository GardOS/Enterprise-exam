package po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Gard on 06.06.2017.
 */
public class HomePageObject extends PageObject{
    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().equals("Home page");
    }

    public HomePageObject toStartingPage() {
        String context = "/exam"; // see jboss-web.xml
        driver.get("localhost:8080" + context + "/home.jsf");
        waitForPageToLoad();

        return this;
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }
}

