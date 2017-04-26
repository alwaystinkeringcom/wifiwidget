package com.alwaystinkering.wifi.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alwaystinkering.wifi.R;
import com.rarepebble.colorpicker.ColorPickerView;

public class IconColorPickerView extends LinearLayout {

    private static final String TAG = "IconColorPickerView";

    private String colorPrefKey;
    private SharedPreferences prefs;

    private TextView headerView;
    private TextView promptText;
    private ImageView iconImage;

    public IconColorPickerView(Context context) {
        super(context);
    }

    public IconColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IconColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initializeView(String header, String prompt, String colorPrefKey, int drawableResource) {
        this.colorPrefKey = colorPrefKey;
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        headerView = (TextView) findViewById(R.id.headerText);
        promptText = (TextView) findViewById(R.id.promptText);
        iconImage = (ImageView) findViewById(R.id.iconImage);

        headerView.setText(header);
        promptText.setText(prompt);
        iconImage.setImageResource(drawableResource);

        final String finalPrefKey = colorPrefKey;

        iconImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPickerView pickerView = new ColorPickerView(getContext());
                new AlertDialog.Builder(getContext())
                        .setView(pickerView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                prefs.edit().putInt(finalPrefKey, pickerView.getColor()).apply();
                            }
                        })
                        .show();
            }
        });
    }

    public void destroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if (s.equals(colorPrefKey)) {
                Log.d(TAG, "Changing icon color to " + String.format("%x", prefs.getInt(colorPrefKey, 0)));
                ColorFilter cf = new PorterDuffColorFilter(prefs.getInt(colorPrefKey, 0), PorterDuff.Mode.OVERLAY);

                iconImage.setColorFilter(cf);
            }
        }
    };
}
