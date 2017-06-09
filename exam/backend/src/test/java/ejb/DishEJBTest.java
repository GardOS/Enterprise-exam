package ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class DishEJBTest extends EJBTestBase {

    @Test
    public void testCreateDish(){
        String name = "name";
        String description = "description";

        assertNotNull(dishEJB.createDish(name, description));
    }

    @Test
    public void testCreateTwoDishes(){
        Long dishCount = dishEJB.countDishes();
        String name = "name";
        String description = "description";

        dishEJB.createDish(name, description);
        dishEJB.createDish(name, description);

        assertTrue(dishEJB.countDishes() == (dishCount + 2));
    }

    //Own

    @Test(expected = EJBException.class)
    public void testWithEmptyName(){
        String name = "";
        String description = "description";

        dishEJB.createDish(name, description);

        fail();
    }
}
