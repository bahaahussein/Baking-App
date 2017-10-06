package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BackingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                               RecipesItem recipesItem , int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.backing_widget_provider);
        views.setTextViewText(R.id.appwidget_title_text, recipesItem.getName());
        views.setTextViewText(R.id.appwidget_text, getIngredientsText(recipesItem.getIngredients()));

        // Create an intent to launch detail activity
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(MainActivity.RECIPES_ITEM_KEY, recipesItem);
        intent.putExtras(extras);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_linear_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                        RecipesItem recipesItem , int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipesItem, appWidgetId);
        }
    }

    private PendingIntent initializeDetailActivityPendingIntet(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static String getIngredientsText(ArrayList<Ingredient> ingredients) {
        String result = "";
        for (Ingredient ingredient : ingredients) {
            result += ingredient.toString() + "\n";
        }
        return result;
    }
}

