package com.alwaystinkering.wifi.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.alwaystinkering.wifi.R;
import com.alwaystinkering.wifi.WifiConfigurationActivity;
import com.alwaystinkering.wifi.util.Log;

public class WifiWidgetProvider extends AppWidgetProvider {

    private final static String TAG = "WifiWidgetProvider";

    private static SharedPreferences prefs;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                WifiWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        updateWidgetView(context, allWidgetIds);
    }

    private void updateWidgetView(Context context, int[] allWidgetIds) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        boolean wifiEnabled = wifiManager.isWifiEnabled();
        boolean wifiConnected = isWifiConnectedToNetwork(wifiInfo);

        Log.v(TAG, "WiFi Enabled: " + wifiEnabled + ", WiFi Connected: " + wifiConnected);

        String ssid = null;
        if (wifiInfo != null && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
            if (ssid != null) {
                // Get rid of the quotes in the SSID name
                ssid = ssid.replace("\"", "");

                // If there is no SSID, <unknown ssid> is returned
                if (ssid.contains("unknown")) {
                    ssid = null;
                }
            }
        }
        //Log.d(TAG, "SSID: " + ssid);

        // Grab current colors out of prefs if they are available
        int connectedColor = prefs.getInt(context.getString(R.string.pref_key_wifi_connected_color), 0xff333333);
        int disconnectedColor = prefs.getInt(context.getString(R.string.pref_key_wifi_disconnected_color), 0xff333333);
        int offColor = prefs.getInt(context.getString(R.string.pref_key_wifi_off_color), 0xff333333);
        int textColor = prefs.getInt(context.getString(R.string.pref_key_wifi_ssid_color), 0xff333333);
        boolean showSsid = prefs.getBoolean(context.getString(R.string.pref_key_wifi_show_ssid), true);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());

        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context
                    .getApplicationContext().getPackageName(),
                    R.layout.wifi_widget);

            // Set the SSID text view
            remoteViews.setTextViewText(R.id.networkText, ssid);
            remoteViews.setInt(R.id.networkText, "setTextColor", textColor);
            remoteViews.setInt(R.id.networkText, "setVisibility", showSsid ? View.VISIBLE : View.GONE);

            // Set the icon and color based on wifi state
            if (wifiEnabled) {
                remoteViews.setImageViewResource(R.id.wifiImage,
                        R.drawable.wifi);

                if (wifiConnected) {
                    remoteViews.setInt(R.id.wifiImage, "setColorFilter", connectedColor);
                } else {
                    remoteViews.setInt(R.id.wifiImage, "setColorFilter", disconnectedColor);
                }
            } else {
                remoteViews.setImageViewResource(R.id.wifiImage,
                        R.drawable.wifi_off);
                remoteViews.setInt(R.id.wifiImage, "setColorFilter", offColor);
            }

            // Open configuration on click of the widget
            Intent configIntent = new Intent(context.getApplicationContext(), WifiConfigurationActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, configIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.wifiImage, configPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private boolean isWifiConnectedToNetwork(WifiInfo wifiInfo) {
        boolean connected = false;

        if (wifiInfo != null) {
            // Check SSID
            if (!wifiInfo.getSSID().contains("unknown")) {
                // See if link strength is above 0 for disconnected
                if (wifiInfo.getLinkSpeed() > 0) {
                    connected = true;
                }
            }
        }

        return connected;
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
