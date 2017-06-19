package com.awesomegroup.food2fork;

import com.awesomegroup.ingredients.Ingredient;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by patry on 24.04.2017.
 */
@Service
public class Food2fork {
    private String apiKey = "716ca2be43d30cce65e497b5f7ef920e";
    private String searchUrl = "http://food2fork.com/api/search?key=";
    private String getUrl = "http://food2fork.com/api/get?key=";
    private OkHttpClient httpClient;

    private static final Logger LOGGER = Logger.getLogger( Food2fork.class.getName() );

    public Food2fork() {
        httpClient = new OkHttpClient();
    }

    public Food2fork(String key)
    {
        apiKey = key;
        httpClient = new OkHttpClient();
    }

    public F2FSearchResult searchRecipes(String ingredients, char sort, int page){
        String result = getDataFromRequest(searchUrl + apiKey + "&q=" + ingredients + "&sort=" + sort + "&page" + page);
        F2FSearchResult searchResult = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FSearchResult.class);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "searchRecipes\n{0}", e);
        }

        return searchResult;
    }

    public F2FSearchResult searchRecipes(String ingredients){
        String result = getDataFromRequest(searchUrl + apiKey + "&q=" + ingredients);
        F2FSearchResult searchResult = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FSearchResult.class);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "searchRecipes\n{0}", e);
        }

        return searchResult;
}

    public F2FSearchResult findTopRated(){
        String result = getDataFromRequest(searchUrl + apiKey);
        F2FSearchResult searchResult = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FSearchResult.class);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "findTopRated\n{0}", e);

        }

        return searchResult;
    }

    public F2FRecipeRecipe getRecipe(String recipeId) {
        String result  = getDataFromRequest( getUrl + apiKey + "&rId=" + recipeId); // test id 35120
        F2FRecipeRecipe searchResult = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FRecipeRecipe.class);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "getRecipe\n{0}", e);
        }

        return searchResult;
    }

    private String getDataFromRequest(String url){
        String result = "";
        Request request = new Request.Builder().url(url).build();
        Response response;
        try {
            response = httpClient.newCall(request).execute();
            result = response.body().string().toString();
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "getDataFromRequest\n{0}", e);
        }

        return result;
    }

    public Ingredient recipeIngredientToIngredient(String apiIngredient)
    {
        int id = 0;
        String name;
        String numbers = getNumbers(apiIngredient);
        name = apiIngredient.replace(numbers, "");
        LOGGER.log(Level.FINE, "recipeIngredientToIngredient\n{0}", name);
        return Ingredient.create().id(id).name(name).build();
    }

    private String getNumbers(String base){
        StringBuilder bld = new StringBuilder();
        boolean flag = true;
        int i = 0;
        while(flag)
        {
            char current = base.charAt(i);
            if(( current > 46 && current <58 )|| current == 32)
            {
                bld.append(current);
                ++i;
            }
            else{
                flag = false;
            }
        }
        return bld.toString();
    }
}