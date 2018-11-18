package com.example.android.powermonitor;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static TextView statusTextView;
    private static int callCount=0;
    private static Boolean sendSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //устанавливаем настройкам значения "по умолчанию" из файла preferences.xml
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); // added 18.11.2018

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sendSMS = sharedPref.getBoolean("pref_send_sms",false);

        statusTextView = (TextView) findViewById(R.id.status);

        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = chargePlug == BatteryManager.BATTERY_STATUS_CHARGING ||
                chargePlug == BatteryManager.BATTERY_STATUS_FULL;

        if (isCharging) statusTextView.setText("Power connected.");
            else statusTextView.setText("Power disconnected.");
    }

    public static void setStatusText(String statusText) {
        callCount++;
        statusTextView.setText(callCount+" : "+statusText+" Send SMS "+sendSMS);
    }

    public void changeSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
