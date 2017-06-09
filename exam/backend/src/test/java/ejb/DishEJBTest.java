package ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
}
