package com.example.android.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Professor on 9/19/2017.
 */

public class RecipesItem implements Parcelable {
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public RecipesItem() {}

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

    public void initializeIngredients(int size) {
        ingredients = new ArrayList<Ingredient>(size);
    }

    public ArrayList<Step> getSteps() {
        return steps;
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

    class Ingredient implements Parcelable{
        private int quantity;
        private String measure;
        private String ingredient;

        public Ingredient(){}

        @Override
        public String toString() {
            return quantity + " " + measure + " " + ingredient;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
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
            parcel.writeInt(quantity);
            parcel.writeString(measure);
            parcel.writeString(ingredient);
        }

        public Ingredient(Parcel in) {
            quantity = in.readInt();
            measure = in.readString();
            ingredient = in.readString();
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
            @Override
            public Ingredient createFromParcel(Parcel in) {
                return new Ingredient(in);
            }

            @Override
            public Ingredient[] newArray(int size) {
                return new Ingredient[size];
            }
        };
    }

    class Step implements Parcelable {
        private String shortDescription;
        private String description;
        private String videoUrl;
        private String thumbnailUrl;

        public Step(){}

        @Override
        public String toString() {
            return  "short description: " + shortDescription + "\ndescription: " +
                    description + "\nvideo url: " + videoUrl + "\nthumbnail url: " + thumbnailUrl;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public Step(Parcel in) {
            shortDescription = in.readString();
            description = in.readString();
            videoUrl = in.readString();
            thumbnailUrl = in.readString();
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator() {
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            public Step[] newArray(int size) {
                return new Step[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(shortDescription);
            parcel.writeString(description);
            parcel.writeString(videoUrl);
            parcel.writeString(thumbnailUrl);
        }
    }

