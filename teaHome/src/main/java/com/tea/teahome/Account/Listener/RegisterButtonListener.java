package com.tea.teahome.Account.Listener;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.tea.teahome.Widget.Toast;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import static com.tea.teahome.Account.Utils.UserUtils.getErrorCode;

/**
 * 注册按钮监控类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 21:10
 */
public class RegisterButtonListener extends BaseButtonListener {
    /**
     * 密码编辑框
     */
    private final EditText et_password;
    /**
     * 密码
     */
    private String password;

    public RegisterButtonListener(Activity activity, EditText et_phoneNum, EditText et_code, EditText et_country_code, EditText et_password) {
        super(activity, et_phoneNum, et_code, et_country_code);
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
            Toast.getToast(activity, "手机号格式错误").show();
        } else {
            //注册手机密码账户
            TuyaHomeSdk.getUserInstance().registerAccountWithPhone(countryCode, phoneNum
                    , password, code, new IRegisterCallback() {
                        @Override
                        public void onSuccess(User user) {
                            Toast.getToast(activity, REGISTER_SUCCESS).show();
                        }

                        @Override
                        public void onError(String code, String error) {
                            Toast.getToast(activity, getErrorCode(code, error)).show();
                        }
                    });
        }
    }

    /**
     * 获得所有组件的文字
     */
    @Override
    public void getText() {
        super.getText();
        password = et_password.getText().toString();
        code = et_code.getText().toString();
    }
}