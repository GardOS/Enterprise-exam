package ejb;

import entity.Dish;
import entity.Menu;
import entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import util.DeleterEJB;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gard on 06.06.2017.
 */
public abstract class EJBTestBase {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "ejb", "entity")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    protected UserEJB userEJB;

    @EJB
    protected DishEJB dishEJB;

    @EJB
    protected MenuEJB menuEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void emptyDatabase(){
        deleterEJB.deleteEntities(User.class);
        deleterEJB.deleteEntities(Dish.class);
        deleterEJB.deleteEntities(Menu.class);
    }

    protected boolean createUser(String userId){
        return userEJB.createUser(userId,"Password","FirstName","LastName");
    }

    protected boolean createUser(String user, String password){
        return userEJB.createUser(user, password,"FirstName","LastName");
    }

    protected List<Dish> getListWithPersistedDish(){
        List<Dish> dishes = new ArrayList<>();
        Long dishId = dishEJB.createDish("name", "description");
        dishes.add(dishEJB.getDish(dishId));
        return dishes;
    }

    protected boolean login(String user, String password){
        return userEJB.login(user, password);
    }
}
