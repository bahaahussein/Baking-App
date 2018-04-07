package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Professor on 9/19/2017.
 */

public class RecipesItem implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<Ingredient> ingredients;
    @SerializedName("steps")
    private ArrayList<Step> steps;

    public RecipesItem() {}

    public RecipesItem(String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    protected RecipesItem(Parcel in) {
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<RecipesItem> CREATOR = new Creator<RecipesItem>() {
        @Override
        public RecipesItem createFromParcel(Parcel in) {
            return new RecipesItem(in);
        }

        @Override
        public RecipesItem[] newArray(int size) {
            return new RecipesItem[size];
        }
    };

    @Override
    public String toString() {
        String result = "name: " + name + "\ningredients:\n";
        for(Ingredient ingredient : ingredients) {
            result += ingredient.toString() + "\n";
        }
        result += "steps:\n";
        for (Step step : steps) {
            result += step.toString() + "\n";
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void initializeIngredients(int size) {
        ingredients = new ArrayList<Ingredient>(size);
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void intializeSteps(int size) {
        steps = new ArrayList<Step>(size);
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
    }
}

