package com.awesomegroup.recipeingredient;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientMeasurement;
import com.awesomegroup.recipe.Recipe;

import javax.persistence.*;

/**
 * Created by Michal on 2017-04-18.
 */
@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @Id
    @GeneratedValue
    @Column(name = "recipe_ingredient_id", nullable = false, unique = true)
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "recipe_ingredient_measurement")
    private IngredientMeasurement measurement;

    @Column(name = "recipe_ingredient_count")
    private Integer count;

    public long getId() {
        return id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public IngredientMeasurement getMeasurement() {
        return measurement;
    }

    public Integer getCount() {
        return count;
    }

    public static RecipeIngredient.Builder create() {
        return new RecipeIngredient.Builder();
    }

    public static class Builder {
        private RecipeIngredient recipeIngredient = new RecipeIngredient();

        public Builder recipe(Recipe recipe) {
            recipeIngredient.recipe = recipe;
            return this;
        }

        public Builder measurement(IngredientMeasurement measurement) {
            recipeIngredient.measurement = measurement;
            return this;
        }

        public Builder ingredient(Ingredient ingredient) {
            recipeIngredient.ingredient = ingredient;
            return this;
        }

        public Builder count(Integer count) {
            recipeIngredient.count = count;
            return this;
        }

        public RecipeIngredient build() {
            return recipeIngredient;
        }
    }
}
