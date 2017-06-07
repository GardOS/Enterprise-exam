import ejb.DishEJB;
import entity.Dish;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Gard on 07.06.2017.
 */
@Named
@SessionScoped
public class DishController implements Serializable{

    @EJB
    private DishEJB dishEJB;

    private String formName;
    private String formDescription;

    public void createDish() {
        dishEJB.createDish(formName, formDescription);
    }

    public Dish getDish(Long dishId){
        return dishEJB.getDish(dishId);
    }

    public void removeDish(Long dishId) {
        dishEJB.removeDish(dishId);
    }

    public long countDishes() {
        return dishEJB.countDishes();
    }

    public List<Dish> getAllDishes(){
        return dishEJB.getAllDishes();
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }
}