package com.lundqvist.oscar.strength;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.lundqvist.oscar.strength.data.Contract;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(Contract.SHARED_REPFS, MODE_PRIVATE);
        String programName = prefs.getString(Contract.CURRENT_PROGRAM, "No Program");

        int currentWorkout = prefs.getInt(Contract.CURRENT_WORKOUT, 1);
        int currentLength = prefs.getInt(Contract.CURRENT_LENGTH, 0)*7;
        String completion = String.valueOf(currentWorkout) + " / " + String.valueOf(currentLength);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.programName, programName);
        views.setTextViewText(R.id.weekCount, completion);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

