package com.tea.teahome.Setting.Fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.tea.teahome.R;
import com.tea.teahome.Setting.Activity.SettingActivity;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.enums.TempUnitEnum;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-04-13 15:21
 */
public class UISettingFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener {
    public UISettingFragment() {
        SettingActivity.SETTING_STATE = "UISettingFragment";
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_display, rootKey);
        addListener();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackground(getResources().getDrawable(R.color.app_bg_color));
        return v;
    }

    private void addListener() {
        EditTextPreference editTextPreference = findPreference("control_timer_max");
        findPreference("temp_unit").setOnPreferenceChangeListener(this);
        if (editTextPreference != null) {
            editTextPreference.setOnPreferenceChangeListener(this);
            editTextPreference.setOnBindEditTextListener(
                    editText -> {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editText.setSingleLine(true);
                        editText.setSelection(editText.getText().length());
                    });
        }
    }

    /**
     * Called when a preference has been changed by the user. This is called before the state
     * of the preference is about to be updated and before the state is persisted.
     *
     * @param preference The changed preference
     * @param newValue   The new value of the preference
     * @return {@code true} to update the state of the preference with the new value
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue == null || ((String) newValue).length() == 0) return false;

        String key = preference.getKey();
        switch (key) {
            case "control_timer_max":
                int value = Integer.parseInt((String) newValue);
                return value > 0 && value < Integer.MAX_VALUE;
            case "temp_unit":
                TempUnitEnum tempUnitEnum = newValue.equals("C") ? TempUnitEnum.Celsius : TempUnitEnum.Fahrenheit;
                TuyaHomeSdk.getUserInstance().setTempUnit(tempUnitEnum, null);
                return true;
            default:
                return true;
        }
    }
}