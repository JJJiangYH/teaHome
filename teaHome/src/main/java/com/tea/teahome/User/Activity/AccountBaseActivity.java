package com.tea.teahome.User.Activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tea.teahome.R;
import com.tea.teahome.User.Bean.AccountBean;
import com.tea.teahome.User.Listener.CodeButtonListener;
import com.tea.teahome.User.Listener.LoginButtonListener;
import com.tea.teahome.User.Listener.PasswordLoginButtonListener;
import com.tea.teahome.User.Listener.RegisterButtonListener;
import com.tea.teahome.User.Utils.AccountBundleUtils;

import static com.tea.teahome.Knowledge.Utils.StringUtils.removeAddSign;
import static com.tea.teahome.Utils.ViewUtil.getActivityFromView;

/**
 * 登录的基础类
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-25 17:56
 */
public class AccountBaseActivity extends AppCompatActivity {
    /**
     * 注册模式
     */
    static final String REGISTER_MODE = "REGISTER";
    /**
     * 密码登录模式
     */
    static final String LOGIN_PASSWORD_MODE = "LOGIN_PASSWORD_MODE";
    /**
     * 验证码登录模式
     */
    static final String LOGIN_CODE_MODE = "LOGIN_CODE_MODE";
    /**
     * 定义一个用来设置按钮的handler
     */
    Handler handler;
    /**
     * 手机号编辑框
     */

    EditText et_phoneNum;
    /**
     * 手机验证码编辑框
     */
    EditText et_code = null;
    /**
     * 手机号国家代码
     */
    EditText et_country_code;
    /**
     * 密码编辑框
     */
    EditText et_password = null;
    /**
     * 登陆的按钮
     */
    Button bt_submit;
    /**
     * 获取验证码的按钮
     */
    Button bt_getCode;
    /**
     * 手机号字符串
     */
    String phoneNum;
    /**
     * 手机验证码字符串
     */
    String code;
    /**
     * 手机号国家代码字符串
     */
    String countryCode;
    /**
     * 密码
     */
    String password;
    /**
     * 存放手机号和验证码信息的类
     */
    AccountBean accountBean;
    private String MODE;

    //    void initHandler() {
//        handler = new ChangeButtonHandler(this, bt_getCode, bt_submit
//                , et_phoneNum, et_code, et_country_code, et_password);
//    }
    void init(String MODE) {
        this.MODE = MODE;
        findViewsById();
        initHandler();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSharedElementExitTransition(null);
    }

    void initHandler() {
        handler = new Handler() {
            /**
             * 依据字符串是否为空，更改布局
             * Subclasses must implement this to receive messages.
             */
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    //手机号字符串为空
                    case 0:
                        setCantClick(bt_getCode, R.color.black_50);
                        break;
                    //手机号字符串为非空
                    case 1:
                        setCanClick(bt_getCode, R.color.black, new CodeButtonListener
                                (AccountBaseActivity.this, et_phoneNum, et_country_code));
                        break;
                    //验证码字符串为空
                    case 2:
                        //验证码和密码为空
                    case 4:
                        //密码或手机号为空
                    case 6:
                        setCantClick(bt_submit, R.color.black_50);
                        break;
                    //验证码字符串非空
                    case 3:
                        setCanClick(bt_submit, R.color.black, new LoginButtonListener
                                (AccountBaseActivity.this, et_phoneNum, et_code, et_country_code));
                        break;
                    //验证码和密码均不为空
                    case 5:
                        setCanClick(bt_submit, R.color.black, new RegisterButtonListener(
                                AccountBaseActivity.this, et_phoneNum, et_code, et_country_code, et_password));
                        break;
                    //手机和密码均不为空
                    case 7:
                        setCanClick(bt_submit, R.color.black, new PasswordLoginButtonListener(
                                AccountBaseActivity.this, et_phoneNum, et_password, et_country_code));
                        break;
                }
            }
        };
    }

    public void setCanClick(Button button, int color, View.OnClickListener onClickListener) {
        button.setHintTextColor(getActivityFromView(button).getResources().getColor(color));
        button.setOnClickListener(onClickListener);
    }

    public void setCantClick(Button button, int color) {
        button.setHintTextColor(getActivityFromView(button).getResources().getColor(color));
        button.setClickable(false);
        button.setOnClickListener(null);
    }

    /**
     * 获得所有组件的文字
     */
    public void getText() {
        switch (MODE) {
            case LOGIN_PASSWORD_MODE:
                password = et_password.getText().toString();
                break;
            case LOGIN_CODE_MODE:
                code = et_code.getText().toString();
                break;
            case REGISTER_MODE:
                password = et_password.getText().toString();
                code = et_code.getText().toString();
        }

        phoneNum = et_phoneNum.getText().toString();
        countryCode = removeAddSign(et_country_code.getText().toString());
    }

    /**
     * 查找所有的组件
     */
    void findViewsById() {
        switch (MODE) {
            case LOGIN_CODE_MODE:
                et_phoneNum = findViewById(R.id.et_phone_num);
                et_code = findViewById(R.id.et_code);
                et_country_code = findViewById(R.id.et_phone_code);
                bt_getCode = findViewById(R.id.bt_getCode);
                bt_submit = findViewById(R.id.bt_login);
                break;
            case REGISTER_MODE:
                et_phoneNum = findViewById(R.id.et_phone_num_register);
                et_code = findViewById(R.id.et_code_register);
                et_country_code = findViewById(R.id.et_phone_code_register);
                et_password = findViewById(R.id.et_password_register);
                bt_getCode = findViewById(R.id.bt_getCode_register);
                bt_submit = findViewById(R.id.bt_register);
                break;
            case LOGIN_PASSWORD_MODE:
                et_phoneNum = findViewById(R.id.et_phone_num_login_password);
                et_password = findViewById(R.id.et_password);
                et_country_code = findViewById(R.id.et_phone_code_password);
                bt_submit = findViewById(R.id.bt_login_password);
                break;
        }
    }

    /**
     * 设置手机号和验证码文本
     *
     * @author jiang yuhang
     * @date 2021-02-25 13:35
     **/
    public void setInfFromBundle() {
        setInfFromBundle(getBundle());
    }

    /**
     * 设置手机号和验证码文本
     *
     * @author jiang yuhang
     * @date 2021-02-25 13:35
     **/
    public void setInfFromBundle(Bundle bundle) {
        if (bundle != null) {
            accountBean = new AccountBean(bundle);
            setInf();
        }
    }

    public void setInf() {
        if (stringIsNullOrEmpty(accountBean.getCode()) && et_code != null) {
            et_code.setText(accountBean.getCode());
        } else {
            if (stringIsNullOrEmpty(accountBean.getPassword()) && et_password != null) {
                et_password.setText(accountBean.getPassword());
            }
        }
        if (stringIsNullOrEmpty(accountBean.getPhoneNum())) {
            et_phoneNum.setText(accountBean.getPhoneNum());
        }
    }

    /**
     * 获取登录界面的手机号和验证码所在的包裹
     *
     * @return 获得的Bundle
     * @date 2021-02-25 13:25
     */
    private Bundle getBundle() {
        return this.getIntent().getExtras();
    }

    /**
     * 判断字符串是否为空或长度为0
     *
     * @param s 要判断的字符串
     * @return true 字符串不为空，且长度大于零 false 字符串为空或长度为零
     */
    private boolean stringIsNullOrEmpty(String s) {
        return s != null && s.length() != 0;
    }

    /**
     * 更改窗口模式（密码登录）（验证码登录）监听器
     *
     * @param view 监控的窗口
     * @author jiang yuhang
     * @date 2021-02-25 23:02
     **/
    public void changeModeListener(View view) {
        Button button = (Button) view;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        String buttonText = button.getText().toString();
        ActivityOptions options;

        if ("密码登录".equals(buttonText)) {
            //now activity is LoginAccountActivity
            intent.setClass(this, LoginAccountByPasswordActivity.class);
            AccountBundleUtils.putTag(bundle, "LoginAccountActivity");
            options = ActivityOptions
                    .makeSceneTransitionAnimation(this,
                            Pair.create(this.findViewById(R.id.CL_login), "CL_login"));
        } else {
            //now activity is LoginAccountByPasswordActivity
            intent.setClass(this, LoginAccountActivity.class);
            AccountBundleUtils.putTag(bundle, "LoginAccountByPasswordActivity");
            options = ActivityOptions
                    .makeSceneTransitionAnimation(this,
                            Pair.create(this.findViewById(R.id.CL_login), "CL_login"));
        }

        getText();
        AccountBundleUtils.putPhoneNum(bundle, phoneNum);
        intent.putExtras(bundle);
        startActivity(intent, options.toBundle());
        this.finishAfterTransition();
    }

    /**
     * 启动注册页面
     *
     * @param view 监听窗口
     */
    public void startRegisterActivityListener(View view) {
        Intent intent = new Intent(this, RegisterAccountActivity.class);
        Bundle bundle = new Bundle();
        ActivityOptions options;

        //修改过度动画
        options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(this.findViewById(R.id.CL_login), "CL_login"));

        getText();
        //塞入包裹
        AccountBundleUtils.putCode(bundle, code);
        AccountBundleUtils.putPassword(bundle, password);
        AccountBundleUtils.putPhoneNum(bundle, phoneNum);
        AccountBundleUtils.putCountryCode(bundle, countryCode);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0, options.toBundle());
    }

    /**
     * 接受返回数据
     *
     * @author jiang yuhang
     * @date 2021-02-26 1:16
     **/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            setInfFromBundle(data.getExtras());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}