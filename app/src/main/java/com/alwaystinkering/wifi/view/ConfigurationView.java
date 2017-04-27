package com.alwaystinkering.wifi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.alwaystinkering.wifi.R;

public class ConfigurationView extends ScrollView {

    public ConfigurationView(Context context) {
        super(context);
    }

    public ConfigurationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConfigurationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initialize() {
        // Initialize each view
        IconColorPickerView connectedView = (IconColorPickerView) findViewById(
                R.id.connectedColorLayout);
        connectedView.initializeView("Connected",
                getContext().getString(R.string.pick_connected_color_text),
                getContext().getString(R.string.pref_key_wifi_connected_color),
                R.drawable.wifi);

        IconColorPickerView disconnectedView = (IconColorPickerView) findViewById(
                R.id.disconnectedColorLayout);
        disconnectedView.initializeView("Disconnected",
                getContext().getString(R.string.pick_disconnected_color_text),
                getContext().getString(R.string.pref_key_wifi_disconnected_color),
                R.drawable.wifi);

        IconColorPickerView offView = (IconColorPickerView) findViewById(R.id.offColorLayout);
        offView.initializeView("Off",
                getContext().getString(R.string.pick_off_color_text),
                getContext().getString(R.string.pref_key_wifi_off_color),
                R.drawable.wifi_off);

        TextColorPickerView textView = (TextColorPickerView) findViewById(R.id.textColorLayout);
        textView.initializeView("Network SSID",
                getContext().getString(R.string.show_network_checkbox_text),
                getContext().getString(R.string.pref_key_wifi_show_ssid),
                getContext().getString(R.string.pref_key_wifi_ssid_color));
    }
}
