package ejb;

import entity.Dish;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Gard on 07.06.2017.
 */
@Stateless
public class DishEJB implements Serializable{
    @PersistenceContext
    private EntityManager em;

    public DishEJB() {
    }

    public boolean createDish(String name, String description) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty()){
            return false;
        }

        Dish dish = new Dish();
        dish.setName(name);
        dish.setDescription(description);
        em.persist(dish);
        return true;
    }

    public void removeDish(@NotNull Long dishId) {
        Dish dish = em.find(Dish.class, dishId);
        if (dish != null) {
            em.remove(dish);
        }
    }

    public List<Dish> getAllDishes() {
        Query query = em.createQuery("SELECT dish FROM Dish dish");
        return query.getResultList();
    }

    public Long countDishes() {
        Query query = em.createQuery("SELECT COUNT(dish) FROM Dish dish");
        return (Long)query.getSingleResult();
    }
}
