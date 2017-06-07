import ejb.MenuEJB;
import entity.Dish;
import entity.Menu;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Gard on 07.06.2017.
 */
@Named
@SessionScoped
public class MenuController implements Serializable {

    @EJB
    private MenuEJB menuEJB;

    private LocalDate formDate;
    private ArrayList<Dish> dishes;

    public void createMenu(){
        menuEJB.createMenu(formDate, dishes);
    }

    public Menu getClosestMenu(){
        return menuEJB.getClosestMenu();
    }

    public Menu getClosestMenuInFuture(){
        return menuEJB.getClosestMenuInFuture(formDate);
    }

    public Menu getClosestMenuInPast(){
        return menuEJB.getClosestMenuInPast(formDate);
    }
}
