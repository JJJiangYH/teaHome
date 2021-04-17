package com.tea.teahome.User.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tea.teahome.R;
import com.tea.teahome.Widget.Toast;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tea.teahome.R.layout.activity_user_inf_show;
import static com.tea.teahome.User.Utils.UserUtils.getRegFrom;
import static com.tea.teahome.User.Utils.UserUtils.getTempUnit;
import static com.tea.teahome.User.Utils.UserUtils.updateUserNickName;
import static com.tea.teahome.Utils.ViewUtil.addStatusBar;

/**
 * 用户信息显示
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-03-02 17:54
 */
public class UserInfActivity extends AppCompatActivity
        implements View.OnClickListener {
    private final Context context;
    /**
     * 昵称
     */
    @BindView(R.id.et_nickName_set)
    EditText et_nickName;
    /**
     * UID
     */
    @BindView(R.id.et_uid_set)
    EditText et_uid;
    /**
     * 手机号
     */
    @BindView(R.id.et_phone)
    EditText et_phone;
    /**
     * 国家代码
     */
    @BindView(R.id.et_phone_code_set)
    EditText et_phone_code;
    /**
     * 国家区域
     */
    @BindView(R.id.et_region_code_set)
    EditText et_region_code;
    /**
     * 注册方式
     */
    @BindView(R.id.et_reg_from)
    EditText et_reg_from;
    /**
     * 时区
     */
    @BindView(R.id.et_time_zone)
    TextView et_time_zone;
    /**
     * 温度单位
     */
    @BindView(R.id.et_temp_unit)
    EditText et_temp_unit;

    public UserInfActivity() {
        this.context = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_user_inf_show);
        ButterKnife.bind(this);
        updateUserInf();
        addStatusBar(this, R.id.ll_user_inf, R.color.statusBar_color);
    }

    /**
     * 显示用户信息
     *
     * @author jiang yuhang
     * @date 2021-03-30 12:01
     */
    private void updateUserInf() {
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (user != null) {
            et_nickName.setText(user.getNickName());
            et_uid.setText(user.getUid());
            et_phone.setText(user.getMobile());
            et_phone_code.setText(user.getPhoneCode());
            et_region_code.setText(user.getDomain().getRegionCode());
            et_reg_from.setText(getRegFrom(user.getRegFrom()));
            et_time_zone.setText(user.getTimezoneId());
            et_temp_unit.setText(getTempUnit(user.getTempUnit()));
        }
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            et_time_zone.setText(data.getStringExtra("selected_item"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @OnClick({R.id.back, R.id.save, R.id.et_time_zone})
    @Override
    public void onClick(View v) {
        if (v.getTag() == null) {
            return;
        }

        String s = v.getTag().toString();

        if ("save".equals(s)) {
            String nickName = et_nickName.getText().toString();
            String timeZone = et_time_zone.getText().toString();
            String lastNickName = TuyaHomeSdk.getUserInstance().getUser().getNickName();
            String lastTimeZone = TuyaHomeSdk.getUserInstance().getUser().getTimezoneId();

            //昵称更改了
            updateUserNickName(this, nickName, new IReNickNameCallback() {
                @Override
                public void onSuccess() {
                    TuyaHomeSdk.getUserInstance().getUser().setNickName(nickName);
                    TuyaHomeSdk.getUserInstance().updateTimeZone(timeZone, new IResultCallback() {
                        @Override
                        public void onSuccess() {
                            TuyaHomeSdk.getUserInstance().getUser().setTimezoneId(timeZone);
                            Toast.getToast(UserInfActivity.this, "修改成功").show();
                        }

                        @Override
                        public void onError(String code, String error) {
                            Toast.getToast(UserInfActivity.this, "修改失败").show();
                        }
                    });
                }

                @Override
                public void onError(String code, String error) {
                    Toast.getToast(UserInfActivity.this, "修改失败").show();
                }
            });
            this.finish();
        } else if ("time_zone".equals(s)) {
            Intent intent = new Intent(UserInfActivity.this, UserInfZoneActivity.class);
            startActivityForResult(intent, 0);
            return;
        } else if ("back".equals(s)) {
            this.finish();
        }
    }
}