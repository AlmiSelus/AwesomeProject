package com.awesomegroup.food2fork;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by patry on 24.04.2017.
 */
@Service
public class Food2fork {
    private String apiKey = "716ca2be43d30cce65e497b5f7ef920e";
    private OkHttpClient httpKlient;

    public Food2fork() {
        httpKlient = new OkHttpClient();
    }

    public Food2fork(String key)
    {
        apiKey = key;
        httpKlient = new OkHttpClient();
    }

    public String searchRecipes(String ingridients, char sort, int page)
    {
        String result = getDataFromRequest("http://food2fork.com/api/search?key=" + apiKey + "&q=" + ingridients + "&sort=" + sort + "&page" + page);
        F2FSearchResult searchResult = new Gson().fromJson(result, F2FSearchResult.class);
        return searchResult.toString();
    }

    public String searchRecipes(String ingridients)
    {
        String result = getDataFromRequest("http://food2fork.com/api/search?key=" + apiKey + "&q=" + ingridients);
        F2FSearchResult searchResult = new Gson().fromJson(result, F2FSearchResult.class);
        return searchResult.toString();
    }

    public F2FSearchResult findTopRated() {
        String result = getDataFromRequest("http://food2fork.com/api/search?key=" + apiKey);
        return new Gson().fromJson(result, F2FSearchResult.class);
    }

    public F2FRecipeRecipe getRecipe(String recipeId)
    {
        String result = getDataFromRequest( "http://food2fork.com/api/get?key=" + apiKey + "&rId=" + recipeId);
        //String result = getDataFromRequest( "http://food2fork.com/api/get?key=" + apiKey + "&rId=" + 35120); //test line - chicken dish id
        F2FRecipeRecipe searchResult = new Gson().fromJson(result, F2FRecipeRecipe.class);
        return searchResult;
    }

    private String getDataFromRequest(String url)
    {
        String result = "";
        Request request = new Request.Builder().url(url).build();

        Response response = null;
        try {
            response = httpKlient.newCall(request).execute();
            result = response.body().string().toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}