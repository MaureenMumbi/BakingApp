package com.example.android.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailActivity;
import com.example.android.data.RecipeList;
import com.example.android.data.Steps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.android.bakingapp.MainActivity.CLICKED_RECIPE;
import static com.example.android.bakingapp.RecipeDetailActivity.CLICKED_INDEX;
import static com.example.android.bakingapp.RecipeDetailActivity.SELECTED_STEPS;


public class RecipeStepsDetailFragment extends Fragment {

    ArrayList<RecipeList>  recipeLists;
    private ArrayList<Steps> recipeSteps = new ArrayList<>();
    private int clickedIndex;
    String recipeName;
    private static String SELECTED_INDEX="Selected_Index";
    private  static  String VIDEO_POSITION="video_position";

    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;

    private SimpleExoPlayer mExoPlayer;
    Uri videouri;
    int recipeID=0;

    @BindView(R.id.recipeStepDetails)TextView recipe_step_detail_textView;
    @BindView(R.id.recipeImage)ImageView recipeImageView;
    @BindView(R.id.exoplayerView)SimpleExoPlayerView mexoPlayerView;
    @BindView(R.id.previousStep)Button mPreviousStep;
    @BindView(R.id.nextStep)Button mNextStep;


    public RecipeStepsDetailFragment() {
        // Required empty public constructor
    }

    private ListItemClickListener listItemClickListener;
    public interface  ListItemClickListener{
        void onListItemClick(List<Steps> recipeSteps, int selectedRecipeIndex);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shouldAutoPlay = true;

        listItemClickListener =(RecipeDetailActivity)getActivity();

        recipeLists = new ArrayList<>();
        if(savedInstanceState != null) {
           recipeSteps = savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            clickedIndex = savedInstanceState.getInt(CLICKED_INDEX);
            recipeName = savedInstanceState.getString("RecipeName");
            //https://stackoverflow.com/questions/45481775/exoplayer-restore-state-when-resumed
           resumePosition = savedInstanceState.getLong(VIDEO_POSITION, C.TIME_UNSET);

        }else {
            recipeSteps = getArguments().getParcelableArrayList(SELECTED_STEPS);
            if (recipeSteps != null) {
                recipeSteps = getArguments().getParcelableArrayList(SELECTED_STEPS);
                clickedIndex = getArguments().getInt(CLICKED_INDEX);
                recipeName = getArguments().getString("RecipeName");
            } else {
                recipeLists = getArguments().getParcelableArrayList(CLICKED_RECIPE);
                recipeSteps = (ArrayList<Steps>) recipeLists.get(0).getSteps();
                clickedIndex = 0;
            }
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps_detail, container, false);

        ButterKnife.bind(this,rootView);
        String recipeVideoURL  = "";
        String recipeImageURL = "";
        String recipeDescription ="";

        recipeDescription =recipeSteps.get(clickedIndex).getDescription();
        recipeID =recipeSteps.get(clickedIndex).getId();
        recipeVideoURL = recipeSteps.get(clickedIndex).getVideoURL();
        recipeImageURL = recipeSteps.get(clickedIndex).getThumbnailURL();

        recipe_step_detail_textView.setText(recipeDescription);
           recipe_step_detail_textView.setVisibility(View.VISIBLE);


              if(!TextUtils.isEmpty(recipeImageURL)){
                Uri image_uri = Uri.parse(recipeImageURL).buildUpon().build();
                Picasso.with(getContext()).load(image_uri).into(recipeImageView);
            }
            if (!TextUtils.isEmpty(recipeVideoURL) ){
                videouri = Uri.parse(recipeVideoURL);
                initializePlayer(videouri);
               }
            else {
                mExoPlayer=null;
                mexoPlayerView.setVisibility(View.GONE);
                recipe_step_detail_textView.setText(recipeDescription);

            }

       return rootView;
    }



    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mexoPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            //https://github.com/google/ExoPlayer/blob/release-v2/demos/main/src/main/java/com/google/android/exoplayer2/demo/PlayerActivity.java
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                mExoPlayer.seekTo(resumeWindow, resumePosition);

            }

            mExoPlayer.prepare(mediaSource, !haveResumePosition, false);
           // mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    private void updateResumePosition() {
        resumeWindow = mExoPlayer.getCurrentWindowIndex();
        resumePosition =  mExoPlayer.getCurrentPosition();
    }



    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_STEPS,recipeSteps);
        currentState.putInt(CLICKED_INDEX,clickedIndex);
        currentState.putString("RecipeName",recipeName);
        currentState.putLong(VIDEO_POSITION, resumePosition);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer!=null){

        shouldAutoPlay = mExoPlayer.getPlayWhenReady();
            updateResumePosition();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
        }
    }





    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23  && mExoPlayer !=null) {
            initializePlayer(videouri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 && mExoPlayer == null) {
            initializePlayer(videouri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer!=null) {
             resumePosition = mExoPlayer.getCurrentPosition();

       if (Util.SDK_INT <= 23 ) {
           releasePlayer();

       }}
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23  ) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }




    @OnClick(R.id.nextStep)
    public void NextStepClick() {
        int lastIndex = recipeSteps.size() - 1;
        int nextIndex =recipeID +1;
        if (recipeSteps.get(clickedIndex).getId() < recipeSteps.get(lastIndex).getId()) {
            if (mExoPlayer != null) {
                mExoPlayer.stop();
            }
            listItemClickListener.onListItemClick(recipeSteps,nextIndex);
            mNextStep.setVisibility(View.VISIBLE);
        } else {
            mNextStep.setVisibility(View.INVISIBLE);
        }



    }

    @OnClick(R.id.previousStep)
    public void PreviousStepClick() {
        if ( recipeID > 0) {
            if (mExoPlayer != null) {
                mExoPlayer.stop();
            }
            listItemClickListener.onListItemClick(recipeSteps, recipeID - 1);
            mPreviousStep.setVisibility(View.VISIBLE);
        } else {
            mPreviousStep.setVisibility(View.INVISIBLE);

        }



    }



}
