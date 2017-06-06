import org.junit.Test;
import po.CreateUserPageObject;
import po.HomePageObject;
import po.LoginPageObject;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gard on 06.06.2017.
 */
public class UserIT extends WebTestBase{
    @Test
    public void testHomePage(){
        home.toStartingPage();
        assertTrue(home.isOnPage());
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
