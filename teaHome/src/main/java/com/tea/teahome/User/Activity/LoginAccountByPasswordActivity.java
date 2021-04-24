package com.tea.teahome.User.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.tea.teahome.R;
import com.tea.teahome.User.Watcher.LoginPasswordViewWatcher;

import static com.tea.view.Utils.ViewUtil.addCloseButtonListener;

public class LoginAccountByPasswordActivity extends AccountBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);
        init(LOGIN_PASSWORD_MODE);
        //设置信息
        setInfFromBundle();
        //添加监听器
        addChangedListener();
        addCloseButtonListener(this, R.id.login_password_close);
    }

    @SuppressLint("NewApi")
    private void addChangedListener() {
        et_phoneNum.addTextChangedListener(new LoginPasswordViewWatcher(handler, et_phoneNum));
        et_password.addTextChangedListener(new LoginPasswordViewWatcher(handler, et_password));
    }
}