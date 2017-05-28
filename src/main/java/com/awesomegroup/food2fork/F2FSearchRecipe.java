package com.awesomegroup.food2fork;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by patry on 24.04.2017.
 */
public class F2FSearchRecipe {
    private String publisher;
    @JsonProperty("publisher")
    public void setPublisher(String data)
    {
        this.publisher = data;
    }
    private String f2fUrl;
    @JsonProperty("f2f_url")
    public void setForkUrl(String data) { this.f2fUrl = data; }
    private String title;
    @JsonProperty("title")
    public void setTitle(String data)
    {
        this.title = data;
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

    public String toString() {
        return title + "/" + recipeId + "/";
    }
}
