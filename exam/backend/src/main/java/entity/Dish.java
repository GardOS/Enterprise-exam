package entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Dish {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Size(min = 1, max = 32)
    private String name;
    @NotNull
    @Size(max = 128)
    private String description;
    @ManyToMany(mappedBy = "dishes")
    private List<Menu> menus;

    public Dish() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}