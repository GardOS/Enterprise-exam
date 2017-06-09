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
        return driver.getTitle().equals("MyCantina Home Page");
    }

    public HomePageObject toStartingPage() {
        String context = "/my_cantina"; // see jboss-web.xml
        driver.get("localhost:8080" + context + "/home.jsf");
        waitForPageToLoad();

        return this;
    }

    public void clickDefaultLink(){
        if (driver.findElement(By.id("defaultLink")) != null){
            driver.findElement(By.id("defaultLink")).click();
        }
    }

    public void clickNextLink(){
        if (driver.findElement(By.id("nextLink")) != null){
            driver.findElement(By.id("nextLink")).click();
        }
    }

    public void clickPreviousLink(){
        if (driver.findElement(By.id("previousLink")) != null){
            driver.findElement(By.id("previousLink")).click();
        }
    }

    public String getCurrentMenuDate(){
        return driver.findElement(By.id("currentMenuDate")).getText();
    }

    public int countDishesInMenu(){
        return driver.findElements(By.xpath("//table[@id='menuTable']/tbody/tr")).size();
    }

    public boolean checkIfTableContainsName(String name){
        return name.equals(driver.findElement(By.xpath("//table[@id='menuTable']/tbody/tr/td[contains(text(),'" + name + "')]")).getText());
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }

    public DishPageObject toDishes() {
        driver.findElement(By.id("dishLink")).click();
        waitForPageToLoad();
        return new DishPageObject(driver);
    }

    public MenuPageObject toMenu() {
        driver.findElement(By.id("menuLink")).click();
        waitForPageToLoad();
        return new MenuPageObject(driver);
    }
}

