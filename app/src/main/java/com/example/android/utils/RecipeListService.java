package com.example.android.utils;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.data.Ingredients;
import com.example.android.data.RecipeList;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.utils.BakingWidgetProvider.SELECTED_RECIPE_INGREDIENT_LIST;

/**
 * Created by Mauryn on 1/17/2018.
 */

public class RecipeListService extends  IntentService {
    public static final String ACTION_LIST_RECIPE = "com.example.android.widget.action.list_recipe";
    public static final String ACTION_UPDATE_BAKING_WIDGETS = "com.example.android.widget.action.update_listrecipe_widgets";
    public static final String EXTRA_RECIPE_ID = "com.example.android.widget.extra.RECIPE_ID";;

    public RecipeListService() {
        super("RecipeListService");
    }

    public static void startActionShowRecipe(Context context, ArrayList<RecipeList> ingredientList, String recipeName) {


        Intent intent = new Intent(context, RecipeListService.class);
        intent.setAction(ACTION_LIST_RECIPE);
        intent.putParcelableArrayListExtra(SELECTED_RECIPE_INGREDIENT_LIST, ingredientList);
        intent.putExtra("RecipeName", recipeName);

        context.startService(intent);
    }

    /**
     * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateBakingWidgets(Context context) {
        Intent intent = new Intent(context, RecipeListService.class);
        intent.setAction(ACTION_UPDATE_BAKING_WIDGETS);
       // context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();

             if (ACTION_UPDATE_BAKING_WIDGETS.equals(action)) {
                 startActionUpdateBakingWidgets(this);
            }else if(ACTION_LIST_RECIPE.equals(action)){
                 handleActionUpdateBakingWidgets(intent);

             }
        }
    }

    /**
     * Handle action WaterPlant in the provided background thread with the provided
     * parameters.
     */
//    private void handleActionListRecipe(long plantId) {
//
//        // Always update widgets after watering plants
//        startActionUpdatBakingWidgets(this);
//    }



    /**
     * Handle action UpdatePlantWidgets in the provided background thread
     */
    static ArrayList<RecipeList> ingredientsList = new ArrayList<>();
     public static ArrayList<String> selectedingredientsList = new ArrayList<>();
    static String recipeName;

    private void handleActionUpdateBakingWidgets(Intent intent) {
        selectedingredientsList = new ArrayList<>();
        int imgRes = R.drawable.ic_view_list_black_48dp;
        ingredientsList = intent.getExtras().getParcelableArrayList(SELECTED_RECIPE_INGREDIENT_LIST);
        List<Ingredients> ingredients = ingredientsList.get(0).getIngredients();
        String ingredient="";
        ArrayList<String> list   = new ArrayList<>();
        Double qty;
        String measure ="";
        for (Ingredients i : ingredients) {
            ingredient=  i.getIngredient();
            qty =  i.getQuantity();
            measure=   i.getMeasure();
           selectedingredientsList.add(String.format("%s %s of %s %n", qty, measure, ingredient));

            }
        recipeName  = intent.getStringExtra("RecipeName");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        BakingWidgetProvider.updateRecipeWidgets(this, appWidgetManager,ingredientsList ,appWidgetIds, recipeName);

    }
}
