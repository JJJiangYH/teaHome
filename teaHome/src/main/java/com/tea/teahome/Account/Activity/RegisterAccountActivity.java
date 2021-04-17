package com.tea.teahome.Account.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tea.teahome.Account.Utils.AccountBundleUtils;
import com.tea.teahome.Account.Watcher.RegisterViewWatcher;
import com.tea.teahome.R;

import butterknife.OnClick;

/**
 * 注册页面
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-23 13:05
 */
public class RegisterAccountActivity extends AccountBaseActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化
        init(REGISTER_MODE);
        //设置信息
        setInfFromBundle();
        findViewById(R.id.register_close).setOnClickListener(v -> finish());
        addChangedListener();
    }

    /**
     * 添加文本编辑框的监听
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addChangedListener() {
        et_phoneNum.addTextChangedListener(new RegisterViewWatcher(handler, et_phoneNum));
        et_code.addTextChangedListener(new RegisterViewWatcher(handler, et_code));
        et_password.addTextChangedListener(new RegisterViewWatcher(handler, et_password));
    }

    /**
     * Call this when your activity is done and should be closed.  The
     * ActivityResult is propagated back to whoever launched you via
     * onActivityResult().
     */
    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        getText();
        //塞入包裹
        AccountBundleUtils.putCode(bundle, code);
        AccountBundleUtils.putPassword(bundle, password);
        AccountBundleUtils.putPhoneNum(bundle, phoneNum);
        AccountBundleUtils.putCountryCode(bundle, countryCode);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}