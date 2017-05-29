package com.awesomegroup.food2fork;

import com.fasterxml.jackson.annotation.JsonProperty;

public class F2FRecipeRecipe
{
    private F2FRecipeResult recipe;
    @JsonProperty("recipe")
    public void setRecipe(F2FRecipeResult data)
    {
        this.recipe = data;
    }
    public F2FRecipeResult getRecipee() {return this.recipe; }

    public String toString()
    {
        return recipe.toString();
    }
}
