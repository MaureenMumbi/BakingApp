package com.example.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.data.RecipeList;
import com.example.android.data.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 1/3/2018.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecyclerDetailViewHolder>{
    List<Steps> stepsList;
    String recipeName="";
    private ListItemClickListener clickListener;


    public interface  ListItemClickListener{
        void onListItemClick(List<Steps> recipeSteps, int clickedItemIndex);
    }

    public RecipeDetailAdapter(ListItemClickListener  listItemClickListener){
        clickListener =listItemClickListener;
    }



    public void setStepsRecipeData(List<RecipeList> recipesIn, Context context) {
        stepsList= recipesIn.get(0).getSteps();
        recipeName=recipesIn.get(0).getName();
        notifyDataSetChanged();
    }
    @Override
    public RecyclerDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_step_detail;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParentImmediately);
        RecyclerDetailViewHolder recyclerViewHolder = new RecyclerDetailViewHolder(view);
        return  recyclerViewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerDetailViewHolder holder, int position) {
        holder.mShortDesciption.setText(stepsList.get(position).getId()+1 +") "+stepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(stepsList!=null){
            return stepsList.size();
        }else {
            return 0;
        }
    }

    public class RecyclerDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.shortDescription)TextView mShortDesciption;

        public RecyclerDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition  = getAdapterPosition();
            clickListener.onListItemClick(stepsList,clickedPosition);

        }
    }
}
