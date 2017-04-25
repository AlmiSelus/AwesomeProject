package com.awesomegroup.food2fork;

import java.util.List;

/**
 * Created by patry on 25.04.2017.
 */
public class F2FRecipeResult {
    public String publisher;
    public String f2f_url;
    public List<String> ingredients;
    public String source_url;
    public String recipe_id;
    public String image_url;
    public String social_rank;
    public String publisher_url;
    public String title;

    public String toString()
    {
        String result = title + "/";
        for(int i = 0; i < ingredients.size(); ++i)
        {
            result += ingredients.get(i).toString() + "/";
        }
        return result;
    }
}

