package com.awesomegroup.food2fork;

import java.util.List;

/**
 * Created by patry on 24.04.2017.
 */
public class F2FSearchResult {
    public String count;
    public List<F2FSearchRecipe> recipes;

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
