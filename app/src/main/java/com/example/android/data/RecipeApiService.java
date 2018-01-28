package com.example.android.data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mauryn on 12/21/2017.
 */

public interface RecipeApiService {
    @GET("baking.json")
    Call<ArrayList<RecipeList>> getRecipeJson();
}
