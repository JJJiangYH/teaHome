package com.tea.teahome.User.Listener

import android.app.Activity
import android.view.View
import android.widget.EditText
import com.tea.teahome.User.Utils.UserUtils
import com.tea.teahome.Widget.Toast
import com.tuya.smart.android.user.api.IValidateCallback
import com.tuya.smart.home.sdk.TuyaHomeSdk

/**
 * 继承Listener的获取验证码功能监听器
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 16:04
 */
class CodeButtonListener(activity: Activity?, et_phoneNum: EditText?, et_country_code: EditText?) : BaseButtonListener(activity, et_phoneNum!!, et_country_code!!) {
    /**
     * 获取验证码
     *
     * @param v bt_getCode
     * @author jiang yuhang
     * @date 2021-02-22 23:33
     */
    override fun onClick(v: View) {
        text
        //判断手机号码位数
        if (phoneNum!!.length != 11) {
            Toast.getToast(activity, PHONE_ERROR).show()
        } else {
            //获取手机验证码
            TuyaHomeSdk.getUserInstance().getValidateCode(countryCode, phoneNum, object : IValidateCallback {
                override fun onSuccess() {
                    Toast.getToast(activity, PHONE_SENT).show()
                }

                override fun onError(code: String, error: String) {
                    Toast.getToast(activity, UserUtils.getErrorCode(code, error)).show()
                }
            })
        }
    }
}