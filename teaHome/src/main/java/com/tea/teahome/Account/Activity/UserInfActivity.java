package com.tea.teahome.Account.Activity;

import android.app.AppComponentFactory;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tea.teahome.R;
import com.tea.teahome.Widget.Toast;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tea.teahome.Account.Utils.UserUtils.getErrorCode;
import static com.tea.teahome.Account.Utils.UserUtils.getRegFrom;
import static com.tea.teahome.Account.Utils.UserUtils.getTempUnit;
import static com.tea.teahome.Account.Utils.UserUtils.updateUserNickName;
import static com.tea.teahome.R.layout.activity_user_inf_show;
import static com.tea.teahome.Utils.ViewUtil.addStatusBar;
import static com.tea.teahome.Utils.ViewUtil.closeActivity;

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
    EditText et_time_zone;
    /**
     * 温度单位
     */
    @BindView(R.id.et_temp_unit)
    EditText et_temp_unit;
    /**
     * 返回按钮
     */
    @BindView(R.id.back)
    ImageButton back;
    /**
     * 保存按钮
     */
    @BindView(R.id.save)
    Button save;

    public UserInfActivity() {
        this.context = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_user_inf_show);
        ButterKnife.bind(this);
        findViewsById();
        updateUserInf();
        addStatusBar(this, R.id.ll_user_inf, R.color.statusBar_color);
        addListener();
    }

    private void addListener() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    /**
     * 找到所有控件
     *
     * @author jiang yuhang
     * @date 2021-03-30 12:14
     */
    private void findViewsById() {
        et_nickName = findViewById(R.id.et_nickName_set);
        et_uid = findViewById(R.id.et_uid_set);
        et_phone = findViewById(R.id.et_phone);
        et_phone_code = findViewById(R.id.et_phone_code_set);
        et_region_code = findViewById(R.id.et_region_code_set);
        et_reg_from = findViewById(R.id.et_reg_from);
        et_time_zone = findViewById(R.id.et_time_zone);
        et_temp_unit = findViewById(R.id.et_temp_unit);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
    }

    /**
     * 显示用户信息
     *
     * @author jiang yuhang
     * @date 2021-03-30 12:01
     */
    private void updateUserInf() {
        User user = TuyaHomeSdk.getUserInstance().getUser();
        et_nickName.setText(user.getNickName());
        et_uid.setText(user.getUid());
        et_phone.setText(user.getMobile());
        et_phone_code.setText(user.getPhoneCode());
        et_region_code.setText(user.getDomain().getRegionCode());
        et_reg_from.setText(getRegFrom(user.getRegFrom()));
        et_time_zone.setText(user.getTimezoneId());
        et_temp_unit.setText(getTempUnit(user.getTempUnit()));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getTag() == null) {
            return;
        }
        String s = v.getTag().toString();
        if ("save".equals(s)) {
            String nickName = et_nickName.getText().toString();
            String lastNickName = TuyaHomeSdk.getUserInstance().getUser().getNickName();
            //昵称更改了
            if (!lastNickName.equals(nickName)) {
                updateUserNickName(this, nickName,
                        new IReNickNameCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.getToast(context, "修改成功").show();
                            }

                            @Override
                            public void onError(String code, String error) {
                                Toast.getToast(context, getErrorCode(code, error)).show();
                            }
                        });
                TuyaHomeSdk.getUserInstance().getUser().setNickName(nickName);
            }
        }
        this.finish();
    }
}