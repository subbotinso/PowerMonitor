package com.example.android.powermonitor;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    // Preferences file name constant
    public static final String APP_PREFERENCES = "PowerMonitorPreferences";

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    // здесь IDEA ругается про утечку памяти, если переменная statusTextView статическая
    // надо разобраться как сделать ее не статической при условии что мне нужно обращаться к этой переменной
    // посредством вызова метода setStatusText из другого класса PowerConnectionReceiver
    // не понятно как получить в классе PowerConnectionReceiver ссылку на объект MainActivity
    // возможно надо использовать context  .... надо разобраться что такое context и как его можно использовать
    private static TextView statusTextView;
    private static int callCount = 0;
    private static Boolean sendSMS;
    private static String smsAddress = "";
    private static Boolean sendEMail;
    private static String emailAddress = "";
    private static String myName;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //устанавливаем настройкам значения "по умолчанию" из файла preferences.xml
        //возможно эта строка не нужна. закомментарил - проверяю как будет работать без нее
        //PreferenceManager.setDefaultValues(this, R.xml.preferences, false); // added 18.11.2018

        // попробовать в этом месте использовать getSharedPreferences вместо getDefaultSharedPreferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sendSMS = sharedPref.getBoolean("pref_send_sms", false);
        if (sendSMS) {
            smsAddress = sharedPref.getString("sms_address", "");
            if (smsAddress.equals("")) {
                Toast.makeText(this, "SMS address isn't set! Please set phone number!",Toast.LENGTH_LONG).show();
            }
        }

        myName = sharedPref.getString("monitor_name", "");

        statusTextView = (TextView) findViewById(R.id.status);

        Context context = getApplicationContext();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = ((chargePlug == BatteryManager.BATTERY_STATUS_CHARGING) ||
                (chargePlug == BatteryManager.BATTERY_STATUS_FULL));

        if (isCharging) {
            statusTextView.setText("Power connected." + " Send SMS " + sendSMS + " Send SMS to: " + smsAddress);
        }
        else {
            statusTextView.setText("Power disconnected." + " Send SMS " + sendSMS + " Send SMS to: " + smsAddress);
        }
    }

    public static void setStatusText(String statusText) {
        callCount++;
        statusTextView.setText(callCount + " : " + statusText + " Send SMS " + sendSMS + " Send SMS to: " + smsAddress);
    }

    public void changeSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

        sendSMS = sharedPref.getBoolean("pref_send_sms", false);
        if (sendSMS) {
            smsAddress = sharedPref.getString("sms_address", "");
            if (smsAddress.equals("")) {
                Toast.makeText(this, "SMS address isn't set! Please set phone number!",Toast.LENGTH_LONG).show();
            }
        }
        myName = sharedPref.getString("monitor_name", "");
    }

    public static String getSmsAddress() {return smsAddress;}

    public static Boolean getSendSMS() {return sendSMS;}

    public static String getMyName() {return myName; }



}