package com.tea.teahome.Setting.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tea.teahome.R;
import com.tea.teahome.Setting.Activity.SettingActivity;
import com.tea.view.Dialog.DialogFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonSettingFragment extends Fragment implements View.OnClickListener {
    Unbinder unbinder;

    public CommonSettingFragment() {
        SettingActivity.SETTING_STATE = "CommonSettingFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    @OnClick({R.id.bt_voice_setting, R.id.bt_clear_setting, R.id.bt_ui})
    public void onClick(View v) {
        if (v == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.bt_voice_setting:
                replaceFragment(new SettingsFragment());
                ((TextView) getActivity().findViewById(R.id.tv_setting_root)).setText(R.string.voice_setting);
                break;
            case R.id.bt_clear_setting:
                DialogFragment dialogFragment = new DialogFragment(DialogFragment.SETTING_MODE);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "DialogFragment");
                break;
            case R.id.bt_ui:
                replaceFragment(new UISettingFragment());
                ((TextView) getActivity().findViewById(R.id.tv_setting_root)).setText(R.string.ui_setting);
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_root, fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}