package com.tea.teahome.User.Watcher;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.widget.EditText;

import com.tea.teahome.R;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 23:43
 */
public class LoginPasswordViewWatcher extends LoginBaseViewWathcer {
    /**
     * 密码编辑框
     */
    private final EditText et_password;
    /**
     * 手机号编辑框
     */
    private final EditText et_phoneNum;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码长度
     */
    private int passwordLength;

    /**
     * 初始化
     *
     * @author jiang yuhang
     * @date 2021-02-25 23:44
     */
    public LoginPasswordViewWatcher(Handler handler, EditText editText) {
        super(handler, editText);
        et_phoneNum = activity.findViewById(R.id.et_phone_num_login_password);
        et_password = activity.findViewById(R.id.et_password);
    }


    public void getText() {
        phone = et_phoneNum.getText().toString();
        phoneLength = phone.length();
        password = et_password.getText().toString();
        passwordLength = password.length();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getText();
    }

    @Override
    public void afterTextChanged(Editable s) {
        Message message = new Message();
        if (phone == null || phoneLength == 0 || password == null || passwordLength == 0) {
            message.what = 6;
        }
        if (phone != null && phoneLength != 0 && password != null && passwordLength != 0) {
            message.what = 7;
        }
        handler.sendMessage(message);
    }
}