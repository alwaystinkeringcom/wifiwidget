
package com.alwaystinkering.wifi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.alwaystinkering.wifi.view.IconColorPickerView;
import com.alwaystinkering.wifi.view.TextColorPickerView;

public class WifiConfiguration extends AppCompatActivity {

    private SharedPreferences prefs;

    private IconColorPickerView connectedView;
    private IconColorPickerView disconnectedView;
    private IconColorPickerView offView;
    private TextColorPickerView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_configuration);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        connectedView = (IconColorPickerView) findViewById(
                R.id.connectedColorLayout);
        connectedView.initializeView("Connected",
                getString(R.string.pick_connected_color_text),
                getString(R.string.pref_key_wifi_connected_color),
                R.drawable.wifi);

        disconnectedView = (IconColorPickerView) findViewById(
                R.id.disconnectedColorLayout);
        disconnectedView.initializeView("Disconnected",
                getString(R.string.pick_disconnected_color_text),
                getString(R.string.pref_key_wifi_disconnected_color),
                R.drawable.wifi);

        offView = (IconColorPickerView) findViewById(R.id.offColorLayout);
        offView.initializeView("Off",
                getString(R.string.pick_off_color_text),
                getString(R.string.pref_key_wifi_off_color),
                R.drawable.wifi_off);

        textView = (TextColorPickerView) findViewById(R.id.textColorLayout);
        textView.initializeView("Network SSID",
                getString(R.string.show_network_checkbox_text),
                getString(R.string.pref_key_wifi_show_ssid),
                getString(R.string.pref_key_wifi_ssid_color));
    }
}
