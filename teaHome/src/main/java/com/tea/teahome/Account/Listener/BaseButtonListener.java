package com.tea.teahome.Account.Listener;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import static com.tea.teahome.Knowledge.Utils.StringUtils.removeAddSign;

/**
 * 监控类的基础类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 21:13
 */
public abstract class BaseButtonListener implements View.OnClickListener {
    /**
     * 该账号未注册
     */
    public static final String ACCOUNT_ERROR = "该用户不存在";
    /**
     * 重新检查网络连接
     */
    public static final String RECHECK_INTERNET = "请重新检查网络连接";
    /**
     * 手机号错误
     */
    public static final String PHONE_ERROR = "手机号格式错误";
    /**
     * 未注册账号
     */
    public static final String NO_REGISTER = "该账号未注册";
    /**
     * 登陆成功
     */
    public static final String LOGIN_SUCCESS = "登陆成功";
    /**
     * 已发送手机验证码
     */
    public static final String PHONE_SENT = "已发送手机验证码";
    /**
     * 注册成功
     */
    public static final String REGISTER_SUCCESS = "注册成功";
    /**
     * 活动
     */
    Activity activity;
    /**
     * 手机号
     */
    String phoneNum;
    /**
     * 国家代码
     */
    String countryCode;
    /**
     * 手机验证码
     */
    String code;
    /**
     * 手机号编辑框
     */
    EditText et_phoneNum;
    /**
     * 手机验证码编辑框
     */
    EditText et_code;
    /**
     * 手机号国家代码
     */
    EditText et_country_code;

    /**
     * 初始化
     *
     * @author jiang yuhang
     * @date 2021-02-25 21:19
     **/
    BaseButtonListener(Activity activity, EditText et_phoneNum, EditText et_code, EditText et_country_code) {
        this(activity, et_phoneNum, et_country_code);
        this.et_code = et_code;
    }

    BaseButtonListener(Activity activity, EditText et_phoneNum, EditText et_country_code) {
        this.activity = activity;
        this.et_country_code = et_country_code;
        this.et_phoneNum = et_phoneNum;
    }

    /**
     * 获得所有组件的文字
     */
    public void getText() {
        phoneNum = et_phoneNum.getText().toString();
        countryCode = removeAddSign(et_country_code.getText().toString());
    }
}