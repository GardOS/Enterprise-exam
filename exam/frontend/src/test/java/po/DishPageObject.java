package po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Gard on 06.06.2017.
 */
public class DishPageObject extends PageObject{
    public DishPageObject(WebDriver driver) {
        super(driver);
        assertEquals("Dishes", driver.getTitle());
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().equals("Dishes");
    }

    public void createUniqueDish(String name){
        setText("newDishForm:dishName",name);
        driver.findElement(By.id("newDishForm:createDish")).click();
        waitForPageToLoad();
    }

    public int getNumberOfDishesWithName(String name){
        return driver.findElements(By.xpath("//table[@id='dishTable']/tbody/tr")).size();
    }

    public boolean checkIfTableContainsName(String name){
        //return name.equals(driver.findElement(By.xpath("//table[@id='dishTable']/tbody/tr/td[contains(text(),'" + name + "')]")).getText());

        List<WebElement> elements = driver.findElements(By.xpath("//table[@id='dishTable']/tbody/tr"));
        for (WebElement element : elements) {
            List<WebElement> row = element.findElements(By.xpath(("td")));
            if (name.equals(row.get(0).getText())){
                return true;
            }
        }
        return false;
    }
}

