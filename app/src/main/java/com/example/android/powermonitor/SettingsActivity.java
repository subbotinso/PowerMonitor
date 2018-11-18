package com.example.android.powermonitor;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by dns on 18.11.2018.
 */

public class SettingsActivity extends PreferenceActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
