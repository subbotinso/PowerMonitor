package com.example.android.powermonitor;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.BatteryManager.BATTERY_PLUGGED_AC;
import static android.os.BatteryManager.BATTERY_PLUGGED_USB;

/**
 * Created by dns on 03.01.2018.
 */

public class PowerConnectionReceiver extends BroadcastReceiver {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    private Context myContext;
    private SmsManager smsManager = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        myContext = context;

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);


        //
        boolean usbCharge = chargePlug == BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BATTERY_PLUGGED_AC;

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level / (float)scale) * 100;

        boolean powerConnected = intent.getAction().toString().equals("android.intent.action.ACTION_POWER_CONNECTED");
        if (powerConnected) {

            MainActivity.setStatusText(" Power connected. Battery level: "+(int)batteryPct+" %");
            // здесь надо будет добавить проверку длинны результирующего SMS сообщения и, при необходимости, процедуру разбиения сообщения на несколько SMS
            if (MainActivity.getSendSMS()) smsManager.sendTextMessage(MainActivity.getSmsAddress(), null, MainActivity.getMyName()+" Power connected. Battery level: "+(int)batteryPct+" %", null, null);
        }
         else {
             MainActivity.setStatusText(" Power disconnected. Battery level: "+(int)batteryPct+" %");
            // здесь надо будет добавить проверку длинны результирующего SMS сообщения и, при необходимости, процедуру разбиения сообщения на несколько SMS
             if (MainActivity.getSendSMS()) smsManager.sendTextMessage(MainActivity.getSmsAddress(), null, MainActivity.getMyName()+" Power disconnected. Battery level: "+(int)batteryPct+" %", null, null);
        }

    }

}
