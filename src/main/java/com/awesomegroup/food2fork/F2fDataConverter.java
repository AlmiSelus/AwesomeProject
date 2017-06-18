package com.awesomegroup.food2fork;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipe.RecipeDifficulty;
import com.awesomegroup.recipeingredient.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by patry on 16.06.2017.
 */
public final class F2fDataConverter {

    private static Random random = new Random();

    public static Ingredient convertIngredient(String ingredient)
    {
        return Ingredient.create().id(0).name(ingredient).build();
    }

    public static RecipeIngredient convertRecipeIngredient(Ingredient ingredient, int count)
    {
        return RecipeIngredient.create().ingredient(ingredient).count(count).build();
    }

    public static Recipe convertRecipe(F2FRecipeResult recipe)
    {
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for(String ingredient : recipe.getIngridients())
        {
            recipeIngredients.add(convertRecipeIngredient(convertIngredient(ingredient), 0 ));
        }
        return Recipe.create().id(0)
                .difficulty((RecipeDifficulty.findByID(random.nextInt(4))))
                .name(recipe.getTitle())
                .preparationTime( Short.parseShort("0"))
                .ingredients(recipeIngredients.toArray(new RecipeIngredient[recipeIngredients.size()]))
                .servings(Byte.parseByte("0"))
                .sourceUrl(recipe.getSourceUrl())
                .imageUrl(recipe.getImageUrl())
                .build();
    }
}
