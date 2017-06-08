import org.junit.Test;
import po.*;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gard on 06.06.2017.
 */
public class MyCantinaIT extends WebTestBase{

    @Test
    public void testHomePage(){
        home.toStartingPage();
        assertTrue(home.isOnPage());
    }

    @Test
    public void testCreateDish(){
        DishPageObject dishes = home.toDishes();
        assertTrue(dishes.isOnPage());

        String name = getUniqueId();

        assertFalse(dishes.checkIfTableContainsName(name));

        dishes.createUniqueDish(name);

        assertTrue(dishes.checkIfTableContainsName(name));
    }

    @Test
    public void testMenu(){
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

        //Show Default

        assertEquals("Menu for " + LocalDate.now().toString(), home.getCurrentMenuDate());

        assertTrue(home.checkIfTableContainsName(dish1));
        assertTrue(home.checkIfTableContainsName(dish2));
        assertEquals(2, home.countDishesInMenu());
    }

    @Test
    public void testDifferentDates(){

    }

    //Own tests

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
