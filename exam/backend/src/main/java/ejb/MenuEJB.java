package ejb;

import entity.Dish;
import entity.Menu;

import javax.ejb.Stateless;
import javax.el.ELException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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

    public Menu getClosestMenu(){
        LocalDate date = LocalDate.now();
        Menu closestMenu ;

        closestMenu = getMenuByDate(date);
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

    public Menu getClosestMenuInFuture(LocalDate date){
        Menu closestMenu = null;
        Query query = em.createQuery("SELECT menu FROM Menu menu");
        List<Menu> menus = query.getResultList();

        for(Menu menu : menus){
            // Will not get current date because x.isAfter(x) == false
            if (menu.getDate().isAfter(date) && (closestMenu == null || menu.getDate().isBefore(closestMenu.getDate()))){
                closestMenu = menu;
            }
        }
        return closestMenu;
    }

    public Menu getClosestMenuInPast(LocalDate date){
        Menu closestMenu = null;
        Query query = em.createQuery("SELECT menu FROM Menu menu");
        List<Menu> menus = query.getResultList();

        for(Menu menu : menus){
            // Will not get current date because x.isBefore(x) == false
            if (menu.getDate().isBefore(date) && (closestMenu == null || menu.getDate().isAfter(closestMenu.getDate()))){
                closestMenu = menu;
            }
        }
        return closestMenu;
    }

    //Own

    public Menu getMenu(Long menuId){
        return em.find(Menu.class, menuId);
    }

    public Menu getMenuByDate(LocalDate date) throws ELException{
        Query query = em.createQuery("SELECT menu FROM Menu menu WHERE menu.date = :date");
        query.setParameter("date", date);
        List<Menu> menus = query.getResultList();
        //Avoid exception
        if (menus.size() == 0){
            return null;
        }
        return menus.get(0);
    }

    public Menu updateMenu(List<Dish> dishes, LocalDate date){
        Menu menu = getMenuByDate(date);
        menu.setDishes(dishes);
        menu = em.merge(menu);
        return menu;
    }

    public List<Menu> getAllMenus() {
        Query query = em.createQuery("SELECT menu FROM Menu menu");
        return query.getResultList();
    }

}