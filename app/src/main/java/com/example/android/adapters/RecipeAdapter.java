package com.example.android.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.data.RecipeList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mauryn on 12/21/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder>{

    Context mContext;
    final private ListItemClickListener listItemClickListener;
    ArrayList<RecipeList> mrecipeLists;


    public interface ListItemClickListener {
        void onListItemClick(RecipeList clickedItemIndex);
    }
    public RecipeAdapter(ListItemClickListener listener) {

        listItemClickListener = listener;

    }
    public void setRecipeData(ArrayList<RecipeList> recipesIn, Context context) {
        mrecipeLists = recipesIn;
        mContext=context;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_cardview;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachtoParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachtoParentImmediately);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return  recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        String recipeName  = mrecipeLists.get(position).getName();
        holder.mRecipeNameView.setText(recipeName);
        String imageUrl  = mrecipeLists.get(position).getImage();
        if(!TextUtils.isEmpty(imageUrl)){
            Uri uri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(uri).into(holder.mRecipeImageView);
        }

    }

    @Override
    public int getItemCount() {
         if(mrecipeLists!=null){
             return mrecipeLists.size();
        }else {
             return 0;
            }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        @BindView(R.id.recipe_name)TextView mRecipeNameView;
        @BindView(R.id.recipe_image)ImageView mRecipeImageView;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
             int clickedPosition  = getAdapterPosition();


            listItemClickListener.onListItemClick(mrecipeLists.get(clickedPosition));

        }
    }
}
