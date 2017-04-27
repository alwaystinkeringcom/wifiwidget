
package com.alwaystinkering.wifi;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.alwaystinkering.wifi.view.ConfigurationView;
import com.alwaystinkering.wifi.view.IconColorPickerView;
import com.alwaystinkering.wifi.view.TextColorPickerView;
import com.alwaystinkering.wifi.widget.WifiWidgetProvider;

public class WifiConfigurationActivity extends AppCompatActivity {

    private int widgetId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_configuration);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        // Try and hide the action bar
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ConfigurationView configurationView = (ConfigurationView) findViewById(R.id.configurationLayout);
        configurationView.initialize();
    }
}
