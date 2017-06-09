import org.junit.Test;
import org.openqa.selenium.By;
import po.*;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MyCantinaIT extends WebTestBase{

   @Test
    public void testHomePage(){
        assertTrue(home.isOnPage());
    }

    @Test
    public void testCreateDish(){
        //Cant create dish without being logged in
        createAndLogNewUser(getUniqueId());
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());

        String name = getUniqueId();

        assertFalse(dishes.checkIfTableContainsName(name));

        dishes.createUniqueDish(name);

        assertTrue(dishes.checkIfTableContainsName(name));
    }

    @Test
    public void testMenu(){
        createAndLogNewUser(getUniqueId());
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());

        String dish1 = getUniqueId();
        String dish2 = getUniqueId();
        String dish3 = getUniqueId();

        dishes.createUniqueDish(dish1);
        dishes.createUniqueDish(dish2);
        dishes.createUniqueDish(dish3);

        home = home.toStartingPage();
        MenuPageObject menu = home.toMenu();
        assertTrue(menu.isOnPage());

        assertTrue(menu.checkIfTableContainsName(dish1));

        assertTrue(menu.checkIfRowHasCheckbox(dish1));
        assertTrue(menu.checkIfRowHasCheckbox(dish2));
        assertTrue(menu.checkIfRowHasCheckbox(dish3));

        menu.clickCheckbox(dish1);
        menu.clickCheckbox(dish2);

        menu.createUniqueMenu(LocalDate.now());

        home.clickDefaultLink();

        assertEquals("Menu for " + LocalDate.now().toString(), home.getCurrentMenuDate());

        assertTrue(home.checkIfTableContainsName(dish1));
        assertTrue(home.checkIfTableContainsName(dish2));

        assertEquals(2, home.countDishesInMenu());
    }

    @Test
    public void testDifferentDates(){
        createAndLogNewUser(getUniqueId());
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());

        String dish = getUniqueId();
        dishes.createUniqueDish(dish);

        home = home.toStartingPage();
        MenuPageObject menu = home.toMenu();
        assertTrue(menu.isOnPage());

        //Menu 1
        menu.clickCheckbox(dish);
        menu.createUniqueMenu(LocalDate.now().minusDays(1));
        menu = home.toMenu();
        //Menu 2
        menu.clickCheckbox(dish);
        menu.createUniqueMenu(LocalDate.now());
        menu = home.toMenu();
        //Menu 3
        menu.clickCheckbox(dish);
        menu.createUniqueMenu(LocalDate.now().plusDays(1));

        home.clickDefaultLink();
        assertEquals("Menu for " + LocalDate.now().toString(), home.getCurrentMenuDate());

        home.clickNextLink();
        assertEquals("Menu for " + LocalDate.now().plusDays(1).toString(), home.getCurrentMenuDate());

        home.clickPreviousLink();
        assertEquals("Menu for " + LocalDate.now().toString(), home.getCurrentMenuDate());

        home.clickPreviousLink();
        assertEquals("Menu for " + LocalDate.now().minusDays(1).toString(), home.getCurrentMenuDate());
    }

    //Own

    @Test
    public void onlyUsersCanCreateDish(){
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());
        assertTrue(getDriver().findElement(By.id("notLoggedIn")).isDisplayed());

        home.toStartingPage();
        createAndLogNewUser(getUniqueId());

        dishes = home.toDishes();

        String name = getUniqueId();
        assertFalse(dishes.checkIfTableContainsName(name));
        dishes.createUniqueDish(name);
        assertTrue(dishes.checkIfTableContainsName(name));
    }

    @Test
    public void onlyUsersCanCreateMenu(){
        createAndLogNewUser(getUniqueId());
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());

        String dish1 = getUniqueId();
        dishes.createUniqueDish(dish1);

        home = home.toStartingPage();
        home.logout();

        MenuPageObject menu = home.toMenu();
        assertTrue(getDriver().findElement(By.id("notLoggedIn")).isDisplayed());

        home.toStartingPage();
        createAndLogNewUser(getUniqueId());

        menu = home.toMenu();
        assertTrue(menu.isOnPage());
        assertTrue(menu.checkIfTableContainsName(dish1));
        menu.clickCheckbox(dish1);

        menu.createUniqueMenu(LocalDate.now());
        home.clickDefaultLink();

        assertEquals("Menu for " + LocalDate.now().toString(), home.getCurrentMenuDate());

        assertTrue(home.checkIfTableContainsName(dish1));
        assertEquals(1, home.countDishesInMenu());
    }


    @Test
    public void testRemoveDish(){
        createAndLogNewUser(getUniqueId());
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());

        String dishName = getUniqueId();
        dishes.createUniqueDish(dishName);
        assertTrue(dishes.checkIfTableContainsName(dishName));

        dishes.clickDelete(dishName);

        assertFalse(dishes.checkIfTableContainsName(dishName));

    }

    @Test
    public void testCantRemoveDishInMenu(){
        createAndLogNewUser(getUniqueId());
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());

        String dishName = getUniqueId();
        dishes.createUniqueDish(dishName);
        assertTrue(dishes.checkIfTableContainsName(dishName));

        home.toStartingPage();
        MenuPageObject menu = home.toMenu();
        menu.clickCheckbox(dishName);
        menu.createUniqueMenu(LocalDate.now());
        dishes = home.toDishes();

        dishes.clickDelete(dishName);

        assertTrue(dishes.checkIfTableContainsName(dishName));
    }

    @Test
    public void testLoginLink(){
        LoginPageObject login = home.toLogin();
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLoginWrongUser(){
        LoginPageObject login = home.toLogin();
        HomePageObject home = login.clickLogin(getUniqueId(),"password");
        assertNull(home);
        assertTrue(login.isOnPage());
    }

    @Test
    public void testLogin(){
        String userId = getUniqueId();
        createAndLogNewUser(userId, "name", "surname");
        home.logout();

        assertFalse(home.isLoggedIn());
        LoginPageObject login = home.toLogin();
        home = login.clickLogin(userId, "password");

        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(userId));
    }

    @Test
    public void testCreateValidUser(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();
        assertTrue(create.isOnPage());

        String username = getUniqueId();

        HomePageObject home = create.createUser(username,"password","password","firstName","lastName");
        assertNotNull(home);
        assertTrue(home.isOnPage());
        assertTrue(home.isLoggedIn(username));

        home.logout();
        assertFalse(home.isLoggedIn());
    }

    @Test
    public void testCreateUserFailDueToPasswordMismatch(){
        LoginPageObject login = home.toLogin();
        CreateUserPageObject create = login.clickCreateNewUser();

        HomePageObject home = create.createUser(getUniqueId(),"password","differentPassword","firstName","lastName");
        assertNull(home);
        assertTrue(create.isOnPage());
    }

}
