package com.tea.teahome.Account.Watcher;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

/**
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-22 21:07
 */
public class LoginViewWatcher extends LoginBaseViewWathcer {
    /**
     * 初始化变量
     *
     * @param editText 监控的视图
     * @author jiang yuhang
     * @date 2021-02-22 21:57
     **/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoginViewWatcher(Handler handler, EditText editText) {
        super(handler, editText);
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
            //如果字符串大于0则返回2
            if (phoneLength > 0)
                message.what = 1;
        } else {
            //如果验证码为空则返回2
            if (codeLength == 0 || code == null)
                message.what = 2;
            //如果验证码不为空则返回4
            if (codeLength > 0)
                message.what = 3;
        }
        handler.sendMessage(message);
    }
}