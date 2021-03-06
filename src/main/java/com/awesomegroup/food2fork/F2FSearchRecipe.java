package com.awesomegroup.food2fork;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by patry on 24.04.2017.
 */
public class F2FSearchRecipe {
    @JsonProperty("publisher")
    private String publisher;
    public void setPublisher(String data)
    {
        this.publisher = data;
    }
    public String getPublisher() {return this.publisher; }
    @JsonProperty("f2f_url")
    private String f2fUrl;
    public void setForkUrl(String data) { this.f2fUrl = data; }
    public String getPF2fUrl() {return this.f2fUrl; }
    @JsonProperty("title")
    private String title;
    public void setTitle(String data)
    {
        this.title = data;
    }
    public String getTitle() {return this.title; }
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

    public String toString() {
        return title + "/" + recipeId + "/";
    }
}
