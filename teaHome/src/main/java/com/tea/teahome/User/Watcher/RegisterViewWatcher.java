package com.tea.teahome.User.Watcher;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.tea.teahome.R;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 19:33
 */
public class RegisterViewWatcher extends LoginBaseViewWathcer {
    /**
     * 密码长度
     */
    private int passwordLength;
    /**
     * 密码文本串
     */
    private String password;
    /**
     * 手机验证码编辑框
     */
    private EditText et_code = null;
    /**
     * 密码编辑框
     */
    private EditText et_password = null;

    /**
     * @param handler  传输handle
     * @param editText 添加监控的编辑框
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RegisterViewWatcher(Handler handler, EditText editText) {
        super(handler, editText);
        //判断是否为手机号编辑框
        if (!isPhone) {
            isCode = true;
            et_code = activity.findViewById(R.id.et_phone_code_register);
            et_password = activity.findViewById(R.id.et_password_register);
        }
        getText(editText.getText());
        afterTextChanged(editText.getText());
    }

    public void getText(CharSequence s) {
        super.getText(s);
        if (!isPhone) {
            this.code = et_code.getText().toString();
            this.codeLength = code.length();
            this.password = et_password.getText().toString();
            this.passwordLength = password.length();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getText(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Message message = new Message();
        if (isPhone) {
            //如果字符串为空则返回0
            if (phoneLength == 0 || phone == null)
                message.what = 0;
            //如果字符串大于0则返回1
            if (phoneLength > 0)
                message.what = 1;
        } else if (isCode) {
            //如果验证码或密码为空则返回4
            if (codeLength == 0 || code == null || passwordLength == 0 || password == null) {
                message.what = 4;
            } else
                //如果验证码和密码均不为空则返回5
                message.what = 5;
        }
        handler.sendMessage(message);
    }
}