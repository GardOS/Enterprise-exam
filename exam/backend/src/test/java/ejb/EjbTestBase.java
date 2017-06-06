package ejb;

import entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import util.DeleterEJB;

import javax.ejb.EJB;

/**
 * Created by Gard on 06.06.2017.
 */
public abstract class EjbTestBase {

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

    /*
    @EJB
    protected PostEJB postEJB;
    */

    @EJB
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void emptyDatabase(){
        //postEJB.getAllPostsByTime().stream().forEach(p -> deleterEJB.deleteEntityById(Post.class, p.getId()));

        deleterEJB.deleteEntities(User.class);
    }

    protected boolean createUser(String userId){
        return userEJB.createUser(userId,"Password","FirstName","LastName");
    }

    protected boolean createUser(String user, String password){
        return userEJB.createUser(user, password,"FirstName","LastName");
    }

    protected boolean login(String user, String password){
        return userEJB.login(user, password);
    }
}
