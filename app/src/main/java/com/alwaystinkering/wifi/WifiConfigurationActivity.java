package com.alwaystinkering.wifi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alwaystinkering.wifi.service.WifiJobService;
import com.alwaystinkering.wifi.util.Log;
import com.alwaystinkering.wifi.view.ConfigurationView;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class WifiConfigurationActivity extends AppCompatActivity {

    private final static String TAG = "WifiConfigurationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_wifi_configuration);

        // Try and hide the action bar
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ConfigurationView configurationView = findViewById(R.id.configurationLayout);
        configurationView.initialize();

        startNetworkChangeJob();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void startNetworkChangeJob() {
        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        Job job = dispatcher.newJobBuilder()
                .setService(WifiJobService.class)
                .setTag("wifi-job")
                .setLifetime(Lifetime.FOREVER)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setRecurring(true)
                .setReplaceCurrent(true)
                //.setTrigger(Trigger.NOW)
                .setTrigger(Trigger.executionWindow(0, 0))
                .build();
        dispatcher.mustSchedule(job);
        Log.d(TAG, "Firebase Job Scheduled");
    }
}
