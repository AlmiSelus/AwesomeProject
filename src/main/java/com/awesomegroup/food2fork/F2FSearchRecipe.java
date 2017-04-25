package com.awesomegroup.food2fork;

/**
 * Created by patry on 24.04.2017.
 */
public class F2FSearchRecipe {
    public String publisher;
    public String f2f_url;
    public String title;
    public String source_url;
    public String recipe_id;
    public String image_url;
    public String social_rank;
    public String publisher_url;

    public String toString() {
        return title + "/" + recipe_id + "/";
    }
}
