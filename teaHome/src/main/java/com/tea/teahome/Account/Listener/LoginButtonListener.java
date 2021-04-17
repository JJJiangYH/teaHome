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
 * 登录按钮监听
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 15:11
 */
public class LoginButtonListener extends BaseButtonListener {
    /**
     * 初始化函数
     */
    public LoginButtonListener(Activity activity, EditText et_phoneNum, EditText et_code, EditText et_country_code) {
        super(activity, et_phoneNum, et_code, et_country_code);
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
            //进行验证码登录
            TuyaHomeSdk.getUserInstance().loginWithPhone(countryCode, phoneNum, code,
                    new ILoginCallback() {
                        @Override
                        public void onSuccess(User user) {
                            Toast.getToast(activity, LOGIN_SUCCESS).show();
                            activity.finish();
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
        code = et_code.getText().toString();
    }
}
