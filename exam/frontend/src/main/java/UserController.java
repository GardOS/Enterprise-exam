import ejb.UserEJB;
import entity.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Gard on 06.06.2017.
 */
@Named
@SessionScoped
public class UserController implements Serializable{

    @EJB
    private UserEJB userEJB;

    public User getUser(String userId){
        return userEJB.getUser(userId);
    }
}

