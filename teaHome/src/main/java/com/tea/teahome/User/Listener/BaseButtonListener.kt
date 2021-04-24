package com.tea.teahome.User.Listener

import android.app.Activity
import android.view.View
import android.widget.EditText

/**
 * 监控类的基础类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 21:13
 */
abstract class BaseButtonListener internal constructor
(var activity: Activity?, var et_phoneNum: EditText, var et_country_code: EditText) : View.OnClickListener {
    /**
     * 手机号
     */
    @JvmField
    var phoneNum: String? = null

    /**
     * 国家代码
     */
    @JvmField
    var countryCode: String? = null

    /**
     * 手机验证码
     */
    @JvmField
    var code: String? = null

    /**
     * 手机验证码编辑框
     */
    @JvmField
    var et_code: EditText? = null

    /**
     * 初始化
     *
     * @author jiang yuhang
     * @date 2021-02-25 21:19
     */
    internal constructor(activity: Activity?, et_phoneNum: EditText, et_code: EditText?, et_country_code: EditText) : this(activity, et_phoneNum, et_country_code) {
        this.et_code = et_code
    }

    /**
     * 获得所有组件的文字
     */
    open val text: Unit
        get() {
            phoneNum = et_phoneNum.text.toString()
            countryCode = et_country_code.text.toString().replace("+", "")
        }

    companion object {
        /**
         * 该账号未注册
         */
        const val ACCOUNT_ERROR = "该用户不存在"

        /**
         * 重新检查网络连接
         */
        const val RECHECK_INTERNET = "请重新检查网络连接"

        /**
         * 手机号错误
         */
        const val PHONE_ERROR = "手机号格式错误"

        /**
         * 未注册账号
         */
        const val NO_REGISTER = "该账号未注册"

        /**
         * 登陆成功
         */
        const val LOGIN_SUCCESS = "登陆成功"

        /**
         * 已发送手机验证码
         */
        const val PHONE_SENT = "已发送手机验证码"

        /**
         * 注册成功
         */
        const val REGISTER_SUCCESS = "注册成功"
    }
}