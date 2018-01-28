package com.example.android.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.IdlingResource.SimpleIdlingResource;
import com.example.android.adapters.RecipeAdapter;
import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.data.RecipeApiService;
import com.example.android.data.RecipeList;
import com.example.android.data.RecipeRetrofitClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.example.android.adapter.MasterListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //RecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ALL_RECIPES = "All_Recipes";

    private RecipeAdapter recipeAdapter;
    @BindView(R.id.rv_recipe)RecyclerView mRecyclerView;

    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        // Get a reference to the Recyclerview  in the fragment_recipe xml layout file
        ButterKnife.bind(this, rootView);

        recipeAdapter = new RecipeAdapter((MainActivity) getActivity());
        mRecyclerView.setAdapter(recipeAdapter);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        Log.e("Tablet", tabletSize+"-");

        if (checkIfLandscape() || tabletSize) {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
        }
        RecipeApiService iRecipe = RecipeRetrofitClient.getApiService();
        Call<ArrayList<RecipeList>> recipe = iRecipe.getRecipeJson();


        final SimpleIdlingResource idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        recipe.enqueue(new Callback<ArrayList<RecipeList>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeList>> call, Response<ArrayList<RecipeList>> response) {
                Integer statusCode = response.code();


                ArrayList<RecipeList> recipes = response.body();
                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipeAdapter.setRecipeData(recipes, getContext());
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<RecipeList>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());
                Toast.makeText(getContext(),"Error while listing recipe, check your internet connection.",Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;





    }


//    public boolean checkIfTablet(){
//            return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
//        == Configuration.SCREENLAYOUT_SIZE_LARGE;
//              }
   public boolean checkIfLandscape(){
             return getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        }
}



