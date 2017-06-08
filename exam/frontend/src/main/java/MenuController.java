import ejb.DishEJB;
import ejb.MenuEJB;
import entity.Dish;
import entity.Menu;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * Created by Gard on 07.06.2017.
 */
@Named
@RequestScoped
public class MenuController implements Serializable {

    @EJB
    private MenuEJB menuEJB;

    @EJB
    private DishEJB dishEJB;

    private LocalDate formDate;
    private ArrayList<Dish> dishes;
    private Map<Long, Boolean> dishesInMenu;

    @PostConstruct
    public void init(){
        dishes = new ArrayList<>();
        dishesInMenu = new HashMap<>();
        for (Dish dish : dishEJB.getAllDishes()){
            dishesInMenu.put(dish.getId(), false);
        }
    }

    public String createMenu(){
        try {
            for (Long id : dishesInMenu.keySet()) {
                if (dishesInMenu.get(id)) {
                    dishes.add(dishEJB.getDish(id));
                }
            }
            menuEJB.createMenu(formDate, dishes);

            //Clear list/map
            dishes.clear();
            for (Dish dish : dishEJB.getAllDishes()) {
                dishesInMenu.put(dish.getId(), false);
            }
        } catch (Exception e){
            return "menu.jsf";
        }
        return "home.jsf";
    }

    public List<Menu> getAllMenus(){
        return menuEJB.getAllMenus();
    }

    public Menu getClosestMenu(){
        return menuEJB.getClosestMenu();
    }

    public boolean checkIfInvalidDate(String date){
        try{
            LocalDate.parse(date);
            return false;
        }catch (Exception e){

        }
        return true;
    }

    public Menu getClosestMenuInFuture(String date){
        return menuEJB.getClosestMenuInFuture(LocalDate.parse(date));
    }

    public Menu getClosestMenuInPast(String date){
        return menuEJB.getClosestMenuInPast(LocalDate.parse(date));
    }

    public Menu getMenuByDateString(String date){
        return menuEJB.getMenuByDate(LocalDate.parse(date));
    }

    public String getFormDate() {
        if(formDate == null)return "";
        return formDate.toString();
    }

    public void setFormDate(String formDate) {
        this.formDate = LocalDate.parse(formDate);
    }

    public Map<Long, Boolean> getDishesInMenu() {
        return dishesInMenu;
    }
}
