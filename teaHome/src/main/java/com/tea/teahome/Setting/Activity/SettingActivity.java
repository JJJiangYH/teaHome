package com.tea.teahome.Setting.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tea.teahome.R;
import com.tea.teahome.Setting.Fragment.CommonSettingFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tea.teahome.Utils.ViewUtil.addStatusBar;

/**
 * 软件设置界面
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-03-30 18:59
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    public static String SETTING_STATE = "CommonSettingFragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_root);
        ButterKnife.bind(this);
        addStatusBar(this, R.id.ll_setting, R.color.statusBar_color);
    }

    @Override
    @OnClick(R.id.back_setting_root)
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        switch (SETTING_STATE) {
            case "CommonSettingFragment":
                super.onBackPressed();
                break;
            case "SettingsFragment":
            case "UISettingFragment":
                replaceFragment(new CommonSettingFragment());
                ((TextView) this.findViewById(R.id.tv_setting_root)).setText(R.string.setting);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_root, fragment)
                .commit();
    }
}