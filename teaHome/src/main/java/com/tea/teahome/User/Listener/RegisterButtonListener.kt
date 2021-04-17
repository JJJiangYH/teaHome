package com.tea.teahome.User.Listener

import android.app.Activity
import android.view.View
import android.widget.EditText
import com.tea.teahome.User.Utils.UserUtils
import com.tea.teahome.Widget.Toast
import com.tuya.smart.android.user.api.IRegisterCallback
import com.tuya.smart.android.user.bean.User
import com.tuya.smart.home.sdk.TuyaHomeSdk

/**
 * 注册按钮监控类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 21:10
 */
class RegisterButtonListener(activity: Activity?, et_phoneNum: EditText?, et_code: EditText?, et_country_code: EditText?,
                             private val et_password: EditText)
    : BaseButtonListener(activity, et_phoneNum!!, et_code, et_country_code!!) {
    /**
     * 密码
     */
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
            Toast.getToast(activity, "手机号格式错误").show()
        } else {
            //注册手机密码账户
            TuyaHomeSdk.getUserInstance().registerAccountWithPhone(countryCode, phoneNum, password, code, object : IRegisterCallback {
                override fun onSuccess(user: User) {
                    Toast.getToast(activity, REGISTER_SUCCESS).show()
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
            password = et_password.text.toString()
            code = et_code!!.text.toString()
        }
}