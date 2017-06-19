package com.awesomegroup.food2fork;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by patry on 25.04.2017.
 */
public class F2FRecipeResult {
    @JsonProperty("publisher")
    private String publisher;
    public void setPublisher(String data)
    {
        this.publisher = data;
    }
    public String getPublisher() {return this.publisher; }
    @JsonProperty("f2f_url")
    private String f2fUrl;
    public void setF2fUrl(String data)
    {
        this.f2fUrl = data;
    }
    public String getF2fUrl() {return this.f2fUrl; }
    @JsonProperty("ingredients")
    private List<String> ingredients;
    public void setIngredients(List<String> data)
    {
        this.ingredients = data;
    }
    public List<String> getIngridients() {return this.ingredients; }
    @JsonProperty("source_url")
    private String sourceUrl;
    public void setSourceUrl(String data)
    {
        this.sourceUrl = data;
    }
    public String getSourceUrl() {return this.sourceUrl; }
    @JsonProperty("recipe_id")
    private String recipeId;
    public void setRecipeId(String data)
    {
        this.recipeId = data;
    }
    public String getRecipeId() {return this.recipeId; }
    @JsonProperty("image_url")
    private String imageUrl;
    public void setImageUrl(String data)
    {
        this.imageUrl = data;
    }
    public String getImageUrl() {return this.imageUrl; }
    @JsonProperty("social_rank")
    private String socialRank;
    public void setSocialRank(String data)
    {
        this.socialRank = data;
    }
    public String getSocialRank() {return this.socialRank; }
    @JsonProperty("publisher_url")
    private String publisherUrl;
    public void setPublisherUrl(String data)
    {
        this.publisherUrl = data;
    }
    public String getPublisherUrl() {return this.publisherUrl; }
    @JsonProperty("title")
    private String title;
    public void setTitle(String data)
    {
        this.title = data;
    }
    public String getTitle() {return this.title; }
    public String toString()
    {
        StringBuilder bld = new StringBuilder();
        bld.append("F2FRecipeResult");
        for(int i = 0; i < ingredients.size(); ++i)
        {
            bld.append(ingredients.get(i));
        }
        return bld.toString();
    }
}

