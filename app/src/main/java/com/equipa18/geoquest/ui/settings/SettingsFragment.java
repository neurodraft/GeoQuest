package com.equipa18.geoquest.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.equipa18.geoquest.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}