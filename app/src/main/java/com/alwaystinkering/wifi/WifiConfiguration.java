package com.alwaystinkering.wifi;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.alwaystinkering.wifi.view.IconColorPickerView;

public class WifiConfiguration extends AppCompatActivity {

    private SharedPreferences prefs;

    private IconColorPickerView connectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_configuration);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        connectedView = (IconColorPickerView) findViewById(R.id.connectedColorLayout);
        connectedView.initializeView("Icon Color", getString(R.string.pick_connected_color_text), getString(R.string.pref_key_wifi_connected_color), R.drawable.wifi);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connectedView.destroy();
    }
}
