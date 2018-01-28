package com.example.android.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;

import java.util.List;

import static com.example.android.utils.RecipeListService.selectedingredientsList;



/**
 * Created by Mauryn on 1/17/2018.
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;

    List<String> remoteViewingredientsList;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
          remoteViewingredientsList = selectedingredientsList;

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return remoteViewingredientsList.size();

    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_view_item);
       views.setTextViewText(R.id.widget_grid_view_item, remoteViewingredientsList.get(position));
       Intent fillInIntent = new Intent();
       // fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_grid_view_item, fillInIntent);

        return views;

      //  RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.plant_widget);

//        // Update the plant image
//        int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
//        views.setImageViewResource(R.id.widget_plant_image, imgRes);
//        views.setTextViewText(R.id.widget_plant_name, String.valueOf(plantId));
//        // Always hide the water drop in GridView mode
//        views.setViewVisibility(R.id.widget_water_button, View.GONE);
//
//        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
//        Bundle extras = new Bundle();
//        extras.putLong(PlantDetailActivity.EXTRA_PLANT_ID, plantId);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//        views.setOnClickFillInIntent(R.id.widget_plant_image, fillInIntent);
//        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

