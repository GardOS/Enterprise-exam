package ejb;

import entity.Dish;
import entity.Menu;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Gard on 07.06.2017.
 */
@RunWith(Arquillian.class)
public class MenuEJBTest extends EJBTestBase{

    @Test(expected = EJBException.class)
    public void testCreateMenuWithNoDish(){
        menuEJB.createMenu(LocalDate.now(), new ArrayList<>());
    }

    @Test
    public void testGetCurrentMenu(){
        List<Dish> dishes = getListWithPersistedDish();

        Long menuId = menuEJB.createMenu(LocalDate.now(),dishes);
        menuEJB.createMenu(LocalDate.now().plusDays(1), dishes);
        menuEJB.createMenu(LocalDate.now().minusDays(1), dishes);

        assertEquals(menuId, menuEJB.getClosestMenu().getId());
    }

    @Test
    public void testGetAbsentPreviousMenu(){
        List<Dish> dishes = getListWithPersistedDish();

        menuEJB.createMenu(LocalDate.now(),dishes);
        menuEJB.createMenu(LocalDate.now().plusDays(1), dishes);

        assertNull(menuEJB.getClosestMenuInPast(LocalDate.now()));
    }

    @Test
    public void testGetAbsentNextMenu(){
        List<Dish> dishes = getListWithPersistedDish();

        menuEJB.createMenu(LocalDate.now(), dishes);
        menuEJB.createMenu(LocalDate.now().minusDays(1), dishes);

        assertNull(menuEJB.getClosestMenuInFuture(LocalDate.now()));
    }

    @Test
    public void testGetPreviousMenu(){
        List<Dish> dishes = getListWithPersistedDish();

        menuEJB.createMenu(LocalDate.now(),dishes);
        menuEJB.createMenu(LocalDate.now().plusDays(1), dishes);
        Long menuId = menuEJB.createMenu(LocalDate.now().minusDays(1), dishes);

        assertEquals(menuId, menuEJB.getClosestMenuInPast(LocalDate.now()).getId());
    }

    @Test
    public void testThreeMenus(){
        List<Dish> dishes = getListWithPersistedDish();

        Menu today = menuEJB.getMenu(menuEJB.createMenu(LocalDate.now(),dishes));
        Menu tomorrow =  menuEJB.getMenu(menuEJB.createMenu(LocalDate.now().plusDays(1), dishes));
        Menu yesterday = menuEJB.getMenu(menuEJB.createMenu(LocalDate.now().minusDays(1), dishes));

        assertEquals(menuEJB.getClosestMenuInFuture(today.getDate()).getId(), tomorrow.getId());
        assertEquals(menuEJB.getClosestMenuInPast(today.getDate()).getId(), yesterday.getId());

        assertNull(menuEJB.getClosestMenuInFuture(tomorrow.getDate()));
        assertEquals(menuEJB.getClosestMenuInPast(tomorrow.getDate()).getId(), today.getId());

        assertNull(menuEJB.getClosestMenuInPast(yesterday.getDate()));
        assertEquals(menuEJB.getClosestMenuInFuture(yesterday.getDate()).getId(), today.getId());
    }
}
