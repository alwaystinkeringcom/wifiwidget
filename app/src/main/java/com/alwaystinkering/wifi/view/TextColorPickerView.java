
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alwaystinkering.wifi.R;
import com.alwaystinkering.wifi.widget.WifiWidgetProvider;
import com.rarepebble.colorpicker.ColorPickerView;

public class TextColorPickerView extends LinearLayout {

    private static final String TAG = "IconColorPickerView";

    private String colorPrefKey;
    private SharedPreferences prefs;

    private TextView headerView;
    private TextView promptText;
    private CheckBox checkBox;
    private ImageView colorPreview;

    public TextColorPickerView(Context context) {
        super(context);
    }

    public TextColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextColorPickerView(Context context, @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initializeView(final String header, final String prompt,
            final String checkboxPrefKey, final String colorPrefKey) {
        this.colorPrefKey = colorPrefKey;
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        final int currentColor = prefs.getInt(colorPrefKey, 0xff333333);
        final boolean checkboxChecked = prefs.getBoolean(checkboxPrefKey, true);

        headerView = (TextView) findViewById(R.id.headerText);
        promptText = (TextView) findViewById(R.id.promptText);
        checkBox = (CheckBox) findViewById(R.id.showCheckbox);
        colorPreview = (ImageView) findViewById(R.id.iconImage);

        headerView.setText(header);
        promptText.setText(prompt);

        checkBox.setChecked(checkboxChecked);
        colorPreview.setColorFilter(currentColor);

        colorPreview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPickerView pickerView = new ColorPickerView(getContext());
                pickerView.setColor(currentColor);
                new AlertDialog.Builder(getContext())
                        .setView(pickerView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int newColor = pickerView.getColor();
                                prefs.edit().putInt(colorPrefKey, newColor).apply();
                                colorPreview.setColorFilter(newColor);
                                updateWidget();
                            }
                        })
                        .show();
            }
        });
        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        prefs.edit()
                                .putBoolean(checkboxPrefKey, isChecked)
                                .apply();
                        updateWidget();
                    }
                });
    }

    private void updateWidget() {
        getContext()
                .sendBroadcast(new Intent(WifiWidgetProvider.UPDATE_WIDGET));
    }
}
