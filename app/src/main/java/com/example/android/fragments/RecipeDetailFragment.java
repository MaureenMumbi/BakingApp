package com.example.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.adapters.RecipeDetailAdapter;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailActivity;
import com.example.android.data.Ingredients;
import com.example.android.data.RecipeList;
import com.example.android.data.Steps;
import com.example.android.utils.RecipeListService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.MainActivity.CLICKED_RECIPE;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {
    @BindView(R.id.rv_recipe_detail)RecyclerView mRecyclerView;
    @BindView(R.id.recipeIngredientdetails)TextView mRecipeIngredientView;

    private RecipeDetailAdapter recipeDetailAdapter;
    ArrayList<RecipeList>  recipeList;
    String recipeName="";
    public static ArrayList<String> selectedingredientsListx = new ArrayList<>();

   public RecipeDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        recipeList = new ArrayList<>();
        ButterKnife.bind(this,rootView);
       if(savedInstanceState !=null){
            recipeList = savedInstanceState.getParcelableArrayList(CLICKED_RECIPE);

        }else {
            recipeList = getArguments().getParcelableArrayList(CLICKED_RECIPE); // TO BE LOOKED AT FRM PREVIOUS LESSONS

       }
        recipeName = recipeList.get(0).getName();
        List<Ingredients>  ingredients = recipeList.get(0).getIngredients();
        List<Steps>  steps = recipeList.get(0).getSteps();

        String ingredient="";
        Double qty =0.0;
        String measure ="";


        for (Ingredients i : ingredients) {
          ingredient=  i.getIngredient();
          qty =  i.getQuantity();
            measure=   i.getMeasure();
           mRecipeIngredientView.append(String.format("%s %s of %s %n", qty, measure, ingredient));
          selectedingredientsListx.add(String.format("%s %s of %s %n", qty, measure, ingredient));
         }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        recipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailActivity)getActivity());
        mRecyclerView.setAdapter(recipeDetailAdapter);
        recipeDetailAdapter.setStepsRecipeData(recipeList,getContext());

        RecipeListService.startActionShowRecipe(getContext(), recipeList,recipeName);

        return  rootView;
 }
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(CLICKED_RECIPE, recipeList);
        currentState.putString("Title",recipeName);


    }
}
