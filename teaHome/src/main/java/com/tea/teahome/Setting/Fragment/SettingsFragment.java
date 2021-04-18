package com.tea.teahome.Setting.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.tea.teahome.R;
import com.tea.teahome.Setting.Activity.SettingActivity;

public class SettingsFragment extends PreferenceFragmentCompat {
    public SettingsFragment() {
        SettingActivity.SETTING_STATE = "SettingsFragment";
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_nlu, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackground(getResources().getDrawable(R.color.app_bg_color));
        return v;
    }
}