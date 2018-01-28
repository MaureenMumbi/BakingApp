package com.example.android.bakingapp;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


/**
 * Created by Mauryn on 1/21/2018.
 */


@RunWith(AndroidJUnit4.class)
public class ShowIngredientsBasicTest {

    @Rule
    public IntentsTestRule<MainActivity> intentTestRule = new IntentsTestRule <>(MainActivity.class);


    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.


        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = intentTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }


    @Test
    public void  viewTests () {

        onView(ViewMatchers.withId(R.id.rv_recipe)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));

        //



    }



    @Test
    public void  ShowIngredientsWhenRecipeisClicked () {

          //https://developer.android.com/training/testing/espresso/lists.html
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));

    }

    @Test
    public void clickRecipeItem_OpensIngredients() {

        onView(ViewMatchers.withId(R.id.rv_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.rv_recipe_detail)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //https://stackoverflow.com/questions/29656562/espresso-ambiguousviewmatcherexception-when-trying-to-click-a-navigation-button
        onView(allOf(withId(R.id.exoplayerView),isDisplayed()));

    }

@After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}

