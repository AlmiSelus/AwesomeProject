package com.awesomegroup.food2fork;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientMeasurement;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by patry on 24.04.2017.
 */
@Service
public class Food2fork {
    private String apiKey = "716ca2be43d30cce65e497b5f7ef920e";
    private String searchUrl = "http://food2fork.com/api/search?key=";
    private String getUrl = "http://food2fork.com/api/get?key=";
    private OkHttpClient httpKlient;

    public Food2fork() {
        httpKlient = new OkHttpClient();
    }

    public Food2fork(String key)
    {
        apiKey = key;
        httpKlient = new OkHttpClient();
    }

    public F2FSearchResult searchRecipes(String ingridients, char sort, int page){
        String result = getDataFromRequest(searchUrl + apiKey + "&q=" + ingridients + "&sort=" + sort + "&page" + page);
        F2FSearchResult searchResult = null;

            ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FSearchResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResult;
    }

    public F2FSearchResult searchRecipes(String ingridients){
        String result = getDataFromRequest(searchUrl + apiKey + "&q=" + ingridients);
        F2FSearchResult searchResult = null;

            ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FSearchResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResult;
}

    public F2FSearchResult findTopRated(){
        String result = getDataFromRequest(searchUrl + apiKey);
        F2FSearchResult searchResult = null;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FSearchResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResult;
    }

    public F2FRecipeRecipe getRecipe(String recipeId) {
        String result  = getDataFromRequest( getUrl + apiKey + "&rId=" + recipeId);
        //String result = getDataFromRequest( "http://food2fork.com/api/get?key=" + apiKey + "&rId=" + 35120); //test line - chicken dish id
        //F2FRecipeResult searchResult = new Gson().fromJson(result, F2FRecipeRecipe.class);
        F2FRecipeRecipe searchResult = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;

        try {
            rootNode = mapper.readTree(result);
            searchResult = mapper.treeToValue(rootNode, F2FRecipeRecipe.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResult;
    }

    private String getDataFromRequest(String url){
        String result = "";
        Request request = new Request.Builder().url(url).build();

        Response response = null;


        try {
            response = httpKlient.newCall(request).execute();
            result = response.body().string().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Ingredient RecipeIngredientToIngredient(String apiIngredient)
    {
        int id = 0;
        String name = "";
        IngredientMeasurement measurement = IngredientMeasurement.ALL;
        String numbers = GetNumbers(apiIngredient);
        String measure = GetMeasurement(apiIngredient);
        name = apiIngredient.replace(numbers, "");
        name = name.replace(measure, "");
        System.out.println(name);
        return Ingredient.create().id(id).name(name).expireDate(Calendar.getInstance()).availableMeasurements(measurement).build();
    }

    private String GetMeasurement(String base){
        for(IngredientMeasurement measure : IngredientMeasurement.values())
        {
            if(base.contains(measure.getMeasurementName()))
            {
                System.out.println(measure.getMeasurementName());
                return measure.getMeasurementName();
            }
        }
        return "";
    }

    private String GetNumbers(String base){
        String result = "";
        boolean flag = true;
        int i = 0;
        while(flag)
        {
            char current = base.charAt(i);
            if(( current > 46 && current <58 )|| current == 32)
            {
                result += current;
                ++i;
            }
            else{
                flag = false;
            }
        }
        System.out.println(result);
        return result;
    }
}