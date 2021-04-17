package com.tea.teahome.Account.Watcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import static com.tea.teahome.Utils.ViewUtil.getActivityFromView;
import static com.tea.teahome.Utils.ViewUtil.getMaxLength;

/**
 * 登录基础监听类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 21:58
 */
public abstract class LoginBaseViewWathcer implements TextWatcher {
    /**
     * handle进程
     * 0：手机号为空
     * 1：手机号非空
     * 2：验证码为空
     * 3：验证码非空
     */
    final Handler handler;
    final Activity activity;
    /**
     * 是否为手机号编辑框监控
     */
    boolean isPhone = false;
    /**
     * 是否为验证码编辑框监控
     */
    boolean isCode = false;
    /**
     * 是否为密码编辑框监控
     */
    boolean isPassword = false;
    /**
     * 手机号长度
     */
    int phoneLength;
    /**
     * 验证码长度
     */
    int codeLength;
    /**
     * 手机号文本串
     */
    String phone;
    /**
     * 验证码文本串
     */
    String code;

    /**
     * 初始化
     *
     * @author jiang yuhang
     * @date 2021-02-25 22:00
     **/
    @SuppressLint("NewApi")
    LoginBaseViewWathcer(Handler handler, EditText editText) {
        this.handler = handler;
        this.activity = getActivityFromView(editText);
        switch (getMaxLength(editText)) {
            case 11:
                isPhone = true;
                break;
            case 6:
                isCode = true;
                break;
            case 16:
                isPassword = true;
                break;
        }
    }

    public void getText(CharSequence s) {
        if (isPhone) {
            this.phone = s.toString();
            this.phoneLength = s.length();
        } else {
            this.code = s.toString();
            this.codeLength = s.length();
        }
    }
}