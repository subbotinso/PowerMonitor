package com.example.android.powermonitor;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static TextView statusTextView;
    private static int callCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTextView = (TextView) findViewById(R.id.status);

    }

    public static void setStatusText(String statusText) {
        callCount++;
        statusTextView.setText(callCount+" : "+statusText);
    }


}
