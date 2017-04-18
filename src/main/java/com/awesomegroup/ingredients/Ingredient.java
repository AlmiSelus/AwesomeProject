package com.awesomegroup.ingredients;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * Created by Michał on 2017-04-18.
 */
@Entity
@Table(name = "ingredients")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ingredient {

    @Id
    @GeneratedValue
    @Column(name = "ingredient_id", nullable = false, unique = true, updatable = false)
    @JsonProperty("id")
    private long ingredientID;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    @JsonProperty("name")
    private String ingredientName;
}
