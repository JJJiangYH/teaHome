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
 * 密码登录按钮监听器
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 22:17
 */
class PasswordLoginButtonListener(activity: Activity?, et_phoneNum: EditText?, private val et_password: EditText,
                                  et_country_code: EditText?)
    : BaseButtonListener(activity, et_phoneNum!!, et_country_code!!) {
    private var password: String? = null

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
            //手机密码登录
            TuyaHomeSdk.getUserInstance().loginWithPhonePassword(countryCode, phoneNum, password,
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

    override val text: Unit
        get() {
            super.text
            password = et_password.text.toString()
        }
}