package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.IdlingResource.SimpleIdlingResource;
import com.example.android.adapters.RecipeAdapter;
import com.example.android.data.Ingredients;
import com.example.android.data.RecipeList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener{
 public static String CLICKED_RECIPE ="Clicked_Recipe";
    @BindView(R.id.app_toolbar) Toolbar toolbar;

    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    @VisibleForTesting
    @Nullable
    public IdlingResource getIdlingResource(){
        if(simpleIdlingResource == null){
            simpleIdlingResource = new SimpleIdlingResource();

        }
        return simpleIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Baking App");

     }




    @Override
    public void onListItemClick(RecipeList clickedItemIndex) {

        Bundle clickedRecipeBundle  = new Bundle();
        ArrayList<RecipeList> clickedRecipe  = new ArrayList<>();
        clickedRecipe.add(clickedItemIndex);
        clickedRecipeBundle.putParcelableArrayList(CLICKED_RECIPE, clickedRecipe);
        List<Ingredients> ingredients = clickedRecipe.get(0).getIngredients();



        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(clickedRecipeBundle);
        startActivity(intent);


    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
