package com.tea.teahome.Account.Listener;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.tea.teahome.Widget.Toast;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import static com.tea.teahome.Account.Utils.UserUtils.getErrorCode;

/**
 * 继承Listener的获取验证码功能监听器
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 16:04
 */
public class CodeButtonListener extends BaseButtonListener {
    public CodeButtonListener(Activity activity, EditText et_phoneNum, EditText et_country_code) {
        super(activity, et_phoneNum, et_country_code);
    }

    /**
     * 获取验证码
     *
     * @param v bt_getCode
     * @author jiang yuhang
     * @date 2021-02-22 23:33
     **/
    @Override
    public void onClick(View v) {
        getText();
        //判断手机号码位数
        if (phoneNum.length() != 11) {
            Toast.getToast(activity, PHONE_ERROR).show();
        } else {
            //获取手机验证码
            TuyaHomeSdk.getUserInstance().getValidateCode(countryCode
                    , phoneNum, new IValidateCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.getToast(activity, PHONE_SENT).show();
                        }

                        @Override
                        public void onError(String code, String error) {
                            Toast.getToast(activity, getErrorCode(code, error)).show();
                        }
                    });
        }
    }
}
