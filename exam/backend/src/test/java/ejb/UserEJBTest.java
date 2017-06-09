package ejb;

import entity.User;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class UserEJBTest extends EJBTestBase {

    @Test
    public void testCanCreateAUser(){
        String userId = "userId";
        String password = "password";

        boolean created = createUser(userId, password);
        assertTrue(created);
    }

    @Test(expected = EJBException.class)
    public void testCreateAUserWithWrongId(){
        String userId = "Invalid -> !";
        String password = "password";

        createUser(userId, password);
    }

    @Test
    public void testCreateAUserWithNullId(){
        String userId = null;

        assertFalse(createUser(userId));
    }

    @Test
    public void testCreateAUserWithNullPassword(){
        String userId = "userId";
        String password = null;

        assertFalse(createUser(userId, password));
    }

    @Test
    public void testLoginWithNullId(){
        String userId = null;
        String password = "password";

        assertFalse(login(userId, password));
    }

    @Test
    public void testCreateAUserWithEmptyId(){
        String userId = "";

        assertFalse(createUser(userId));
    }

    @Test
    public void testNoTwoUsersWithSameId(){
        String userId = "userId";

        boolean created = createUser(userId,"password");
        assertTrue(created);

        created = createUser(userId,"password");
        assertFalse(created);
    }

    @Test
    public void testSamePasswordLeadToDifferentHashAndSalt(){
        String password = "password";
        String first = "first";
        String second = "second";

        createUser(first,password);
        createUser(second,password); //same password

        User firstUser = userEJB.getUser(first);
        User secondUser = userEJB.getUser(second);

        assertNotEquals(firstUser.getHash(), secondUser.getHash());
        assertNotEquals(firstUser.getSalt(), secondUser.getSalt());
    }

    @Test
    public void testVerifyPassword(){
        String userId = "userId";
        String correct = "correct";
        String wrong = "wrong";

        createUser(userId, correct);

        boolean canLogin = userEJB.login(userId, correct);
        assertTrue(canLogin);

        canLogin = userEJB.login(userId, wrong);
        assertFalse(canLogin);
    }

    @Test
    public void testBeSurePasswordIsNotStoredInPlain(){
        String userId = "userId";
        String password = "password";

        createUser(userId, password);

        User entity = userEJB.getUser(userId);
        assertNotEquals(password, entity.getUserId());
        assertNotEquals(password, entity.getHash());
        assertNotEquals(password, entity.getSalt());
    }
}
