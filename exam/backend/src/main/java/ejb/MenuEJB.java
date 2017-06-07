package ejb;

import entity.Dish;
import entity.Menu;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Gard on 07.06.2017.
 */
@Stateless
public class MenuEJB {
    @PersistenceContext
    private EntityManager em;

    public MenuEJB() {
    }

    public Long createMenu(@NotNull LocalDate date, @NotNull List<Dish> dishes) {
        Menu menu = new Menu();
        menu.setDate(date);
        menu.setDishes(dishes);

        em.persist(menu);
        return menu.getId();
    }

    public Menu getMenu(Long menuId){
        return em.find(Menu.class, menuId);
    }

    //TODO: Optimize
    public Menu getClosestMenu(LocalDate date){
        Menu closestMenu = null;

        closestMenu = getMenu(date, "present");
        if (closestMenu != null){
            return closestMenu;
        }

        closestMenu = getClosestMenuInFuture(date);
        if (closestMenu != null){
            return closestMenu;
        }

        closestMenu = getClosestMenuInPast(date);
        if (closestMenu != null){
            return closestMenu;
        }
        return closestMenu;
    }

    // Will not get current date because x.isAfter(x) == false
    public Menu getClosestMenuInFuture(LocalDate date){
        return getMenu(date, "future");
    }

    // Will not get current date because x.isBefore(x) == false
    public Menu getClosestMenuInPast(LocalDate date){
        return getMenu(date, "past");
    }

    private Menu getMenu(LocalDate date, String when){
        Menu closestMenu = null;
        Query query = em.createQuery("SELECT menu FROM Menu menu");
        List<Menu> menus = query.getResultList();

        switch (when){
            case "present":
                for(Menu menu : menus){
                    if (menu.getDate().isEqual(date)){
                        return menu;
                    }
                }
                break;

            case "future":
                for(Menu menu : menus){
                    if (menu.getDate().isAfter(date) && (closestMenu == null || menu.getDate().isBefore(closestMenu.getDate()))){
                        closestMenu = menu;
                    }
                }
                break;

            case "past":
                for(Menu menu : menus){
                    if (menu.getDate().isBefore(date) && (closestMenu == null || menu.getDate().isAfter(closestMenu.getDate()))){
                        closestMenu = menu;
                    }
                }
                break;
        }
        return closestMenu;
    }
}
