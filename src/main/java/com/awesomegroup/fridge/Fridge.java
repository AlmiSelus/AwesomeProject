package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adi on 26.05.2017.
 */

@Entity
@Table(name = "fridges")
@JsonSerialize
public class Fridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_id", unique = true, nullable = true)
    @JsonProperty("id")
    private long fridgeId;

    @OneToOne (fetch=FetchType.LAZY, mappedBy="fridge")
    private User fridgeUser;

    @ManyToMany
    @JoinTable(
            name = "fridge_ingredients",
            joinColumns = @JoinColumn(name = "fridge_id", referencedColumnName = "fridge_id"),  //name = nowa kolumna, ref = istenijacy klucz
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    )
    private List<Ingredient> fridgeIngredients = new ArrayList<>();

    public long getFridgeId() {
        return fridgeId;
    }

    public User getFridgeUser() {
        return fridgeUser;
    }
 
    public List<Ingredient> getFridgeIngredients() {
        return fridgeIngredients;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private Fridge fridge;

        private Builder() {
            fridge = new Fridge();
        }

        public Builder id(long id) {
            fridge.fridgeId = id;
            return this;
        }

        public Builder user(User user)
        {
            fridge.fridgeUser = user;
            return this;
        }

        public Fridge build() {
            return fridge;
        }

    }

}
