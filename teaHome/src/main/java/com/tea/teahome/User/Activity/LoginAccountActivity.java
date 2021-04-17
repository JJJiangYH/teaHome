package com.tea.teahome.User.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tea.teahome.R;
import com.tea.teahome.User.Watcher.LoginViewWatcher;
import com.tea.teahome.Widget.Toast;
import com.tuya.smart.android.user.api.ILogoutCallback;

import static com.tea.teahome.User.Utils.UserUtils.getErrorCode;
import static com.tea.teahome.User.Utils.UserUtils.logoutAccount;
import static com.tea.teahome.Utils.ViewUtil.addCloseButtonListener;
import static com.tea.teahome.Utils.ViewUtil.addStatusBar;

/**
 * 账号的注册与登录
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-22 11:36
 */
public class LoginAccountActivity extends AccountBaseActivity {
    private Context context;

    public LoginAccountActivity() {
    }

    public LoginAccountActivity(Context context) {
        this.context = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //初始化页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logoutAccount(this, new ILogoutCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String code, String error) {
                Toast.getToast(context, getErrorCode(code, error)).show();
            }
        });

        init(LOGIN_CODE_MODE);
        //添加状态栏遮挡
        addStatusBar(this, R.id.CL_login, R.color.app_bg_color);
        //设置信息
        setInfFromBundle();
        //添加监听
        addChangedListener();
        addCloseButtonListener(this, R.id.login_close);
    }

    /**
     * 添加文本编辑框的监听
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addChangedListener() {
        et_phoneNum.addTextChangedListener(new LoginViewWatcher(handler, et_phoneNum));
        et_code.addTextChangedListener(new LoginViewWatcher(handler, et_code));
    }
}