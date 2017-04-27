
package com.alwaystinkering.wifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.alwaystinkering.wifi.view.IconColorPickerView;
import com.alwaystinkering.wifi.view.TextColorPickerView;
import com.alwaystinkering.wifi.widget.WifiWidgetProvider;

public class WifiConfiguration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_configuration);

        // Update the widget
        sendBroadcast(new Intent(WifiWidgetProvider.UPDATE_WIDGET));

        // Try and hide the action bar
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Initialize each view
        IconColorPickerView connectedView = (IconColorPickerView) findViewById(
                R.id.connectedColorLayout);
        connectedView.initializeView("Connected",
                getString(R.string.pick_connected_color_text),
                getString(R.string.pref_key_wifi_connected_color),
                R.drawable.wifi);

        IconColorPickerView disconnectedView = (IconColorPickerView) findViewById(
                R.id.disconnectedColorLayout);
        disconnectedView.initializeView("Disconnected",
                getString(R.string.pick_disconnected_color_text),
                getString(R.string.pref_key_wifi_disconnected_color),
                R.drawable.wifi);

        IconColorPickerView offView = (IconColorPickerView) findViewById(R.id.offColorLayout);
        offView.initializeView("Off",
                getString(R.string.pick_off_color_text),
                getString(R.string.pref_key_wifi_off_color),
                R.drawable.wifi_off);

        TextColorPickerView textView = (TextColorPickerView) findViewById(R.id.textColorLayout);
        textView.initializeView("Network SSID",
                getString(R.string.show_network_checkbox_text),
                getString(R.string.pref_key_wifi_show_ssid),
                getString(R.string.pref_key_wifi_ssid_color));
    }
}
