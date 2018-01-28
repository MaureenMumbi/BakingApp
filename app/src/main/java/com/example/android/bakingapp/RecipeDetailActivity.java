package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.adapters.RecipeDetailAdapter;
import com.example.android.data.Ingredients;
import com.example.android.data.RecipeList;
import com.example.android.data.Steps;
import com.example.android.fragments.RecipeDetailFragment;
import com.example.android.fragments.RecipeStepsDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.MainActivity.CLICKED_RECIPE;

/**
 * Created by Mauryn on 1/3/2018.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener,RecipeStepsDetailFragment.ListItemClickListener {
    private ArrayList<RecipeList> recipeLists ;
    public static  final String SELECTED_STEPS ="Selected_Steps";
    public static  final  String CLICKED_INDEX ="Clicked_Index";
    public static  final String STACK="Stack";
    @BindView(R.id.app_toolbar) Toolbar toolbar;


    static  boolean mTwoPane;

    String recipeName = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
                Bundle clickedRecipeBundle = getIntent().getExtras();
                recipeLists = new ArrayList<>();
                recipeLists = clickedRecipeBundle.getParcelableArrayList(CLICKED_RECIPE);
                List<Ingredients> ingredients = recipeLists.get(0).getIngredients();


                recipeName = recipeLists.get(0).getName();


            FragmentManager fragmentManager = getSupportFragmentManager();
            final RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(clickedRecipeBundle);
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container, recipeDetailFragment).addToBackStack(null).commit();
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            Log.e("tabletsize","--"+tabletSize);
            if(tabletSize){
                final RecipeStepsDetailFragment fragment2 = new RecipeStepsDetailFragment();
                fragment2.setArguments(clickedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.steps_detail_fragment_container, fragment2).addToBackStack(null)
                        .commit();
                mTwoPane=true;

            }else{
                mTwoPane =false;
            }

        }else {
            recipeName= savedInstanceState.getString("Title");
        }

          setupToolbar();

        }


    private void setupToolbar() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(recipeName);


            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //https://stackoverflow.com/questions/10863572/programmatically-go-back-to-the-previous-fragment-in-the-backstack
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if (fragmentManager.getBackStackEntryCount() > 1) {
                        if (findViewById(R.id.steps_detail_fragment_container)==null) {

                            fragmentManager.popBackStack();}
                        else{
                            finish();
                        }
                    } else {
                        finish();
                    }



                }
            });
        }

        }
//    }

    @Override
    public void onListItemClick(List<Steps> recipeSteps, int clickedItemIndex) {

        RecipeStepsDetailFragment fragment = new RecipeStepsDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle  = new Bundle();
        bundle.putParcelableArrayList(SELECTED_STEPS, (ArrayList<Steps>) recipeSteps);
        bundle.putInt(CLICKED_INDEX,clickedItemIndex);
        fragment.setArguments(bundle);
        if (mTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_detail_fragment_container, fragment).addToBackStack(null)
                    .commit();

        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment).addToBackStack(null)
                    .commit();
        }


    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title",recipeName);
        outState.putParcelableArrayList(CLICKED_RECIPE, recipeLists);
    }
    public boolean checkIfTablet(){

        return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public boolean checkIfLandscape(){
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

}