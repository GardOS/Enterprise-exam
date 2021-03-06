import ejb.DishEJB;
import ejb.MenuEJB;
import entity.Dish;
import entity.Menu;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public String createMenu() throws Exception{
        try {
            if (checkIfInvalidDate(formDate.toString())){
                return "menu.jsf";
            }

            for (Long id : dishesInMenu.keySet()) {
                if (dishesInMenu.get(id)) {
                    dishes.add(dishEJB.getDish(id));
                }
            }
            if (menuEJB.getMenuByDate(formDate) != null){
                menuEJB.updateMenu(dishes, formDate);
            } else {
                menuEJB.createMenu(formDate, dishes);
            }

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

    public Menu getClosestMenu(){
        return menuEJB.getClosestMenu();
    }

    public boolean checkIfInvalidDate(String date){
        try{
            LocalDate.parse(date);
            return false;
        }catch (Exception e){}
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

    public void setFormDate(String formDate){
        if (!checkIfInvalidDate(formDate)){
            this.formDate = LocalDate.parse(formDate);
        }
    }

    public Map<Long, Boolean> getDishesInMenu() {
        return dishesInMenu;
    }
}
