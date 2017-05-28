package com.awesomegroup.food2fork;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by patry on 25.04.2017.
 */
public class F2FRecipeResult {
    private String publisher;
    @JsonProperty("publisher")
    public void setPublisher(String data)
    {
        this.publisher = data;
    }
    private String f2fUrl;
    @JsonProperty("f2f_url")
    public void setF2fUrl(String data)
    {
        this.f2fUrl = data;
    }
    private List<String> ingredients;
    @JsonProperty("ingredients")
    public void setIngredients(List<String> data)
    {
        this.ingredients = data;
    }
    private String sourceUrl;
    @JsonProperty("source_url")
    public void setSourceUrl(String data)
    {
        this.sourceUrl = data;
    }
    private String recipeId;
    @JsonProperty("recipe_id")
    public void setRecipeId(String data)
    {
        this.recipeId = data;
    }
    private String imageUrl;
    @JsonProperty("image_url")
    public void setImageUrl(String data)
    {
        this.imageUrl = data;
    }
    private String socialRank;
    @JsonProperty("social_rank")
    public void setSocialRank(String data)
    {
        this.socialRank = data;
    }
    private String publisherUrl;
    @JsonProperty("publisher_url")
    public void setPublisherUrl(String data)
    {
        this.publisherUrl = data;
    }
    private String title;
    @JsonProperty("title")
    public void setTitle(String data)
    {
        this.title = data;
    }

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

