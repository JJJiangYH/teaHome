package com.tea.teahome.User.Listener

import android.app.Activity
import android.view.View
import android.widget.EditText
import com.tea.teahome.User.Utils.UserUtils
import com.tea.teahome.Widget.Toast
import com.tuya.smart.android.user.api.ILoginCallback
import com.tuya.smart.android.user.bean.User
import com.tuya.smart.home.sdk.TuyaHomeSdk

/**
 * 登录按钮监听
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 15:11
 */
class LoginButtonListener(activity: Activity?, et_phoneNum: EditText?, et_code: EditText?, et_country_code: EditText?)
    : BaseButtonListener(activity, et_phoneNum!!, et_code, et_country_code!!) {
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        text
        //判断手机号位数是否为11位
        if (phoneNum!!.length != 11) {
            Toast.getToast(activity, PHONE_ERROR).show()
        } else {
            //进行验证码登录
            TuyaHomeSdk.getUserInstance().loginWithPhone(countryCode, phoneNum, code,
                    object : ILoginCallback {
                        override fun onSuccess(user: User) {
                            Toast.getToast(activity, LOGIN_SUCCESS).show()
                            activity!!.finish()
                        }

                        override fun onError(code: String, error: String) {
                            Toast.getToast(activity, UserUtils.getErrorCode(code, error)).show()
                        }
                    })
        }
    }

    /**
     * 获得所有组件的文字
     */
    override val text: Unit
        get() {
            super.text
            code = et_code!!.text.toString()
        }
}