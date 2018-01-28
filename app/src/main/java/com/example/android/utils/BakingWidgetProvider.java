package com.example.android.utils;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailActivity;
import com.example.android.data.RecipeList;

import java.util.ArrayList;

import static com.example.android.bakingapp.MainActivity.CLICKED_RECIPE;
import static com.example.android.utils.RecipeListService.ingredientsList;
import static com.example.android.utils.RecipeListService.recipeName;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static String SELECTED_RECIPE_INGREDIENT_LIST ="SELECTED_RECIPE_INGREDIENT_LIST";


static  ArrayList<String> WidgetIngredientList;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                ArrayList<RecipeList> recipelist, int appWidgetId, String recipeName) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        RemoteViews rv;
            rv = getSelectedRecipeRemoteView(context, recipelist,recipeName);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI

        RecipeListService.startActionUpdateBakingWidgets(context);
       }


    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
             ArrayList<RecipeList> recipeList,int[] appWidgetIds, String recipeName) {



        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeList, appWidgetId, recipeName);
        }
    }

    private static RemoteViews getSelectedRecipeRemoteView(Context context, ArrayList<RecipeList> recipeList, String recipeName) {
        // Set the click handler to open the DetailActivity if a recipe has been clicked  plant ID,
        // or the MainActivity if no recipe has been clicked
        Intent intent;
        Log.e("yyyy",recipeList+"");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
             if (recipeList==null || recipeList.size() == 0 ) {

                Intent configIntent = new Intent(context, MainActivity.class);

                PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

                views.setOnClickPendingIntent(R.id.widget_grid_view, configPendingIntent);


        // should open mainActivity home
    } else { // Set on click to open the corresponding detail activity

            // Set the GridWidgetService intent to act as the adapter for the GridView
            intent = new Intent(context, GridWidgetService.class);
            views.setRemoteAdapter(R.id.widget_grid_view, intent);
            // Set the RecipeDetailActivity intent to launch when clicked
            Intent appIntent = new Intent(context, RecipeDetailActivity.class);
            //appIntent.putExtra(CLICKED_RECIPE, recipeList);
            appIntent.putParcelableArrayListExtra(CLICKED_RECIPE, recipeList);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
                 views.setTextViewText(R.id.widget_recipe_name, recipeName);
        }

            return views;

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        RecipeListService.startActionShowRecipe(context, ingredientsList,recipeName);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }


}

