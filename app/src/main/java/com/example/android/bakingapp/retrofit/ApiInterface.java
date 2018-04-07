package com.example.android.bakingapp.retrofit;

import com.example.android.bakingapp.model.RecipesItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Professor on 4/6/2018.
 */

public interface ApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<RecipesItem>> getRecipes();
}
