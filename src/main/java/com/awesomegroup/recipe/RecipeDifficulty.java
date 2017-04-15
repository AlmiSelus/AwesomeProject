package com.awesomegroup.recipe;

import java.util.Arrays;

/**
 * Created by Michał on 2017-04-14.
 */
public enum RecipeDifficulty {

    EASY(0, "Easy"),
    MEDIUM(1, "Medium"),
    HARD(2, "Hard"),
    EXPERT(3, "Expert Chef");

    private byte id;
    private String name;

    RecipeDifficulty(int id, String name) {
        this.id = (byte) id;
        this.name = name;
    }

    public byte getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static RecipeDifficulty findByName(String difficulty) {
        return Arrays.stream(values()).filter(diff->diff.getName().equalsIgnoreCase(difficulty)).findAny().orElse(null);
    }
}
