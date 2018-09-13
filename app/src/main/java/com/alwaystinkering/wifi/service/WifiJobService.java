package com.alwaystinkering.wifi.service;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.alwaystinkering.wifi.util.Log;
import com.alwaystinkering.wifi.widget.WifiWidgetProvider;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class WifiJobService extends JobService {

    private static final String TAG = "WifiJobService";

    BroadcastReceiver connectivityChange;
    ConnectivityManager connectivityManager;

    @Override
    public boolean onStartJob(JobParameters job) {
        //Log.i(TAG, "Job created");
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "Registering Network Callback");
            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), networkCallback);
        }

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        Log.d(TAG, "Registering receiver");
        registerReceiver(connectivityChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateWidget();
            }
        }, filter);


        Log.i(TAG, "Done with onStartJob");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (networkCallback != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            connectivityManager.unregisterNetworkCallback(networkCallback);
        if (connectivityChange != null) unregisterReceiver(connectivityChange);

        Log.i(TAG, "done with onStopJob");
        return true;
    }

    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {

        @Override
        public void onAvailable(Network network) {
            Log.i(TAG, "requestNetwork onAvailable()");
            updateWidget();
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            Log.i(TAG, "requestNetwork onCapabilitiesChanged() " + network + "|" + networkCapabilities);
            updateWidget();
        }

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            Log.i(TAG, "requestNetwork onLinkPropertiesChanged()");
            updateWidget();
        }

        @Override
        public void onLosing(Network network, int maxMsToLive) {
            Log.i(TAG, "requestNetwork onLosing()");
            updateWidget();
        }

        @Override
        public void onLost(Network network) {
            Log.i(TAG, "requestNetwork onLost()");
            updateWidget();
        }
    };

    private void updateWidget() {
        Intent intent = new Intent(this, WifiWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(this);
        int ids[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, WifiWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
