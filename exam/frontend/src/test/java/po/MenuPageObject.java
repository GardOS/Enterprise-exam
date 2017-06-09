package po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class MenuPageObject extends PageObject{
    public MenuPageObject(WebDriver driver) {
        super(driver);
        assertEquals("Menu", driver.getTitle());
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().equals("Menu");
    }

    public void createUniqueMenu(LocalDate date){
        setText("newMenuForm:formDate", date.toString());
        driver.findElement(By.id("newMenuForm:createMenu")).click();
        waitForPageToLoad();
    }

    public boolean checkIfTableContainsName(String name){
        return name.equals(driver.findElement(By.xpath("//table[@id='newMenuForm:dishTable']/tbody/tr/td[contains(text(),'" + name + "')]")).getText());
    }

    // xpath inspiration: http://www.software-testing-tutorials-automation.com/2015/01/selecting-checkbox-from-table-based-on.html
    public boolean checkIfRowHasCheckbox(String name){
        boolean unchecked = driver.findElement(By.xpath("//table[@id='newMenuForm:dishTable']/tbody/tr/td[contains(text(),'" + name + "')]/following-sibling::td/input[@type='checkbox']")).isSelected();
        clickCheckbox(name);
        boolean checked = driver.findElement(By.xpath("//table[@id='newMenuForm:dishTable']/tbody/tr/td[contains(text(),'" + name + "')]/following-sibling::td/input[@type='checkbox']")).isSelected();
        clickCheckbox(name);
        return (checked != unchecked) && checked;
    }

    public void clickCheckbox(String name){
        driver.findElement(By.xpath("//table[@id='newMenuForm:dishTable']/tbody/tr/td[contains(text(),'" + name + "')]/following-sibling::td/input[@type='checkbox']")).click();
    }
}

