package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Professor on 4/6/2018.
 */

public class Ingredient implements Parcelable {
    @SerializedName("quantity")
    private double quantity;
    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredient;

    public Ingredient(){}

    public Ingredient(double q, String m, String i){
        quantity = q;
        measure = m;
        ingredient = i;
    }

    @Override
    public String toString() {
        return quantity + " " + measure + " " + ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @SuppressWarnings("unused")
    public static final Creator<com.example.android.bakingapp.model.Ingredient> CREATOR = new Creator<com.example.android.bakingapp.model.Ingredient>() {
        @Override
        public com.example.android.bakingapp.model.Ingredient createFromParcel(Parcel in) {
            return new com.example.android.bakingapp.model.Ingredient(in);
        }

        @Override
        public com.example.android.bakingapp.model.Ingredient[] newArray(int size) {
            return new com.example.android.bakingapp.model.Ingredient[size];
        }
    };
}
