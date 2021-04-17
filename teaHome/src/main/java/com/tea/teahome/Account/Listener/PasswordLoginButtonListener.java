package com.tea.teahome.Account.Listener;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.tea.teahome.Widget.Toast;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import static com.tea.teahome.Account.Utils.UserUtils.getErrorCode;

/**
 * 密码登录按钮监听器
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 22:17
 */
public class PasswordLoginButtonListener extends BaseButtonListener {
    /**
     * 密码编辑框
     */
    private final EditText et_password;
    /**
     * 密码
     */
    private String password;

    /**
     * 初始化
     */
    public PasswordLoginButtonListener(Activity activity, EditText et_phoneNum, EditText et_password, EditText et_country_code) {
        super(activity, et_phoneNum, et_country_code);
        this.et_password = et_password;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        getText();
        //判断手机号位数是否为11位
        if (phoneNum.length() != 11) {
            Toast.getToast(activity, PHONE_ERROR).show();
        } else {
            //手机密码登录
            TuyaHomeSdk.getUserInstance().loginWithPhonePassword(countryCode, phoneNum, password, new ILoginCallback() {
                @Override
                public void onSuccess(User user) {
                    Toast.getToast(activity, LOGIN_SUCCESS).show();
                    activity.finish();
                }

                @Override
                public void onError(String code, String error) {
                    Toast.getToast(activity, getErrorCode(code,error)).show();
                }
            });
        }
    }

    @Override
    public void getText() {
        super.getText();
        password = et_password.getText().toString();
    }
}