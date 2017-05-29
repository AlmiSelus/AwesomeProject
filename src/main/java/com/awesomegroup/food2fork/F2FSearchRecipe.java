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
    public String getPublisher() {return this.publisher; }
    private String f2fUrl;
    @JsonProperty("f2f_url")
    public void setForkUrl(String data) { this.f2fUrl = data; }
    public String getPF2fUrl() {return this.f2fUrl; }
    private String title;
    @JsonProperty("title")
    public void setTitle(String data)
    {
        this.title = data;
    }
    public String getTitle() {return this.title; }
    private String sourceUrl;
    @JsonProperty("source_url")
    public void setSourceUrl(String data)
    {
        this.sourceUrl = data;
    }
    public String getSourceUrl() {return this.sourceUrl; }
    private String recipeId;
    @JsonProperty("recipe_id")
    public void setRecipeId(String data)
    {
        this.recipeId = data;
    }
    public String getRecipeId() {return this.recipeId; }
    private String imageUrl;
    @JsonProperty("image_url")
    public void setImageUrl(String data)
    {
        this.imageUrl = data;
    }
    public String getImageUrl() {return this.imageUrl; }
    private String socialRank;
    @JsonProperty("social_rank")
    public void setSocialRank(String data)
    {
        this.socialRank = data;
    }
    public String getSocialRank() {return this.socialRank; }
    private String publisherUrl;
    @JsonProperty("publisher_url")
    public void setPublisherUrl(String data)
    {
        this.publisherUrl = data;
    }
    public String getPublisherUrl() {return this.publisherUrl; }

    public String toString() {
        return title + "/" + recipeId + "/";
    }
}
