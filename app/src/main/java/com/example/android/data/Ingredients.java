package com.example.android.data;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.id;

/**
 * Created by Mauryn on 12/21/2017.
 */

public class Ingredients implements Parcelable {
    public Double quantity;
    public String measure;
    public String ingredient;

    public Double getQuantity(){return quantity;};
    private void setQuantity (Double quantity){this.quantity = quantity;}

    public String getMeasure(){return  measure;}
    private void  setMeasure(String measure){this.measure = measure;}

    public String getIngredient(){return  ingredient;}
    private  void  setIngredient(String  ingredient){this.ingredient = ingredient;}

    protected Ingredients(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
}
