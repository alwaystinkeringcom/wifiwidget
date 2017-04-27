
package com.alwaystinkering.wifi.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiWidgetProvider extends AppWidgetProvider {

    private final static String TAG = "WifiWidgetProvider";

    public static final String UPDATE_WIDGET = "com.alwaystinkering.wifi.UPDATE_WIDGET";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                WifiWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(),
                WifiWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
        context.startService(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        Log.d(TAG, "received action: " + action);

        // update all widgets
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, WifiWidgetProvider.class));

        // Update the widget views
        onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
