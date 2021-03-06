package po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LoginPageObject extends PageObject{

    public LoginPageObject(WebDriver driver) {
        super(driver);

        assertEquals("Login", driver.getTitle());
        assertFalse(isLoggedIn());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("Login");
    }

    public HomePageObject clickLogin(String username, String password){
        setText("loginForm:username",username);
        setText("loginForm:password",password);
        driver.findElement(By.id("loginForm:loginButton")).click();
        waitForPageToLoad();

        if(isOnPage()){
            return null;
        } else {
            return new HomePageObject(driver);
        }
    }

    public CreateUserPageObject clickCreateNewUser(){
        driver.findElement(By.id("createUser")).click();
        waitForPageToLoad();
        return new CreateUserPageObject(driver);
    }
}
