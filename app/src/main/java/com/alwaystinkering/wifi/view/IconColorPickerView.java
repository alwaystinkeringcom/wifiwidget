package com.alwaystinkering.wifi.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alwaystinkering.wifi.R;
import com.alwaystinkering.wifi.widget.WifiWidgetProvider;
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

    public void initializeView(final String header, final String prompt, final String colorPrefKey, final int drawableResource) {
        this.colorPrefKey = colorPrefKey;
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final int currentColor = prefs.getInt(colorPrefKey, 0xff333333);

        headerView = (TextView) findViewById(R.id.headerText);
        promptText = (TextView) findViewById(R.id.promptText);
        iconImage = (ImageView) findViewById(R.id.iconImage);

        headerView.setText(header);
        promptText.setText(prompt);
        iconImage.setImageResource(drawableResource);
        iconImage.setColorFilter(currentColor);

        iconImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPickerView pickerView = new ColorPickerView(getContext());
                pickerView.setColor(currentColor);
                new AlertDialog.Builder(getContext())
                        .setView(pickerView)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int newColor = pickerView.getColor();
                                prefs.edit().putInt(colorPrefKey, newColor).apply();
                                iconImage.setColorFilter(newColor);
                                updateWidget();
                            }
                        })
                        .show();
            }
        });
    }

    private void updateWidget() {
        getContext().sendBroadcast(new Intent(WifiWidgetProvider.UPDATE_WIDGET));
    }
}
