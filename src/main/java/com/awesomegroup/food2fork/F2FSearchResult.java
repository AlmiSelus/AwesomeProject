package com.awesomegroup.food2fork;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by patry on 24.04.2017.
 */
public class F2FSearchResult {

    private String count;
    @JsonProperty("count")
    public void setCount(String data) { this.count = data; }
    public int getCount(){return Integer.parseInt(this.count); }
    private List<F2FSearchRecipe> recipes;
    @JsonProperty("recipes")
    public void setRecipes(List<F2FSearchRecipe> data) { this.recipes = data; }
    public List<F2FSearchRecipe> getRecipes() {return this.recipes; }

    public String toString()
    {
        String result = count + "/";
        for(int i = 0; i < Integer.parseInt(count); ++i)
        {
            result += recipes.get(i).toString();
        }
        return result;
    }
}
