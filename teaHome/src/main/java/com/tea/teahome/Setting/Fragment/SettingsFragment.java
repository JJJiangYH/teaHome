package com.tea.teahome.Setting.Fragment;

import android.os.Bundle;

import com.tea.teahome.R;
import com.tea.teahome.Setting.Activity.SettingActivity;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    public SettingsFragment() {
        SettingActivity.SETTING_STATE = "SettingsFragment";
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_nlu, rootKey);
    }
}