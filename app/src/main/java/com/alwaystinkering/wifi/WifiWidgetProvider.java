
package com.alwaystinkering.wifi;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        Log.w("WifiWidgetProvider", "onUpdate method called");
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
        if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION) ||
                action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION) ||
                action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

            // update all widgets
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, WifiWidgetProvider.class));
            onUpdate(context, appWidgetManager, appWidgetIds);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}
