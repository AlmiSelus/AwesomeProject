package com.awesomegroup.food2fork;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by patry on 24.04.2017.
 */
public class F2FSearchResult {

    @JsonProperty("count")
    private String count;
    public void setCount(String data) { this.count = data; }
    public int getCount(){return Integer.parseInt(this.count); }
    @JsonProperty("recipes")
    private List<F2FSearchRecipe> recipes;
    public void setRecipes(List<F2FSearchRecipe> data) { this.recipes = data; }
    public List<F2FSearchRecipe> getRecipes() {return this.recipes; }

    public String toString()
    {
        StringBuilder bld = new StringBuilder();
        bld.append("F2FSearchResult");
        for(int i = 0; i < Integer.parseInt(count); ++i)
        {
            bld.append(recipes.get(i).toString());
        }
        return bld.toString();
    }
}
