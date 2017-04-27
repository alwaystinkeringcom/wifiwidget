
package com.alwaystinkering.wifi.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.alwaystinkering.wifi.R;
import com.alwaystinkering.wifi.WifiConfiguration;

public class WifiWidgetService extends Service {

    private static final String TAG = "WifiWidgetService";

    private SharedPreferences prefs;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Grab a reference to the default shared prefs
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Update the widget views
        updateWidgetView(intent);

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }


    private void updateWidgetView(Intent intent) {
        WifiManager wifiManager = (WifiManager) getApplicationContext()
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

        // Grab current colors out of prefs if they are available
        int connectedColor = prefs.getInt(getString(R.string.pref_key_wifi_connected_color), 0xff333333);
        int disconnectedColor = prefs.getInt(getString(R.string.pref_key_wifi_disconnected_color), 0xff333333);
        int offColor = prefs.getInt(getString(R.string.pref_key_wifi_off_color), 0xff333333);
        int textColor = prefs.getInt(getString(R.string.pref_key_wifi_ssid_color), 0xff333333);
        boolean showSsid = prefs.getBoolean(getString(R.string.pref_key_wifi_show_ssid), true);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(this
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
            Intent configIntent = new Intent(getApplicationContext(), WifiConfiguration.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, configIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.wifiImage, configPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private boolean isWifiConnectedToNetwork(WifiInfo wifiInfo) {
        boolean connected = false;

        if (wifiInfo != null) {
            Log.d(TAG, "Wifi Info: " + wifiInfo);
            // Check SSID
            if (!wifiInfo.getSSID().contains("<unknown ssid>")) {
                // See if link strength is above 0 for disconnected
                if (wifiInfo.getLinkSpeed() > 0) {
                    connected = true;
                }
            }
        }

        return connected;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
