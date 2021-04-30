package com.tea.iot.UserModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizEventType;
import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.tea.iot.CommonModule.GosBaseActivity;
import com.tea.iot.CommonModule.GosDeploy;
import com.tea.iot.CommonModule.TipsDialog;
import com.tea.iot.DeviceModule.GosDeviceListActivity;
import com.tea.iot.DeviceModule.GosMainActivity;
import com.tea.iot.GosApplication;
import com.tea.iot.R;
import com.tea.iot.view.DotView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

@SuppressLint("HandlerLeak")
public class GosUserLoginActivity extends GosUserModuleBaseActivity
        implements OnClickListener {
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
    /**
     * The Wechat
     */
    public static IWXAPI mIwxapi;
    /**
     * The GizThirdAccountType
     */
    public static GizThirdAccountType gizThirdAccountType;
    /**
     * The THRED_LOGIN UID&TOKEN
     */
    public static String thirdUid, thirdToken;
    /**
     * The et Name
     */
    private static EditText etName;
    /**
     * The et Psw
     */
    private static EditText etPsw;
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    /**
     * The Scope
     */
    private final String Scope = "get_user_info,add_t";
    /**
     * 与WXEntryActivity共用Handler
     */
    private final Handler baseHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                // 登录
                case LOGIN:
                    progressDialog.show();
                    GosDeviceListActivity.loginStatus = 0;
                    GizWifiSDK.sharedInstance().userLogin(etName.getText().toString(), etPsw.getText().toString());
                    break;
                // 自动登录
                case AUTO_LOGIN:
                    progressDialog.show();
                    GosDeviceListActivity.loginStatus = 0;
                    GizWifiSDK.sharedInstance().userLogin(spf.getString("UserName", ""), spf.getString("PassWord", ""));
                    break;
                // 第三方登录
                case THRED_LOGIN:
                    progressDialog.show();
                    GosDeviceListActivity.loginStatus = 0;
                    GizWifiSDK.sharedInstance().loginWithThirdAccount(gizThirdAccountType, thirdUid, thirdToken);
                    spf.edit().putString("thirdUid", thirdUid).commit();
                    break;

            }
        }

    };
    /**
     * The IUiListener
     */
    IUiListener listener;
    Intent intent;
    /**
     * The btn Login
     */
    private Button btnLogin;
    /**
     * The tv Register
     */
    private TextView tvRegister;
    /**
     * The tv Forget
     */
    private TextView tvForget;
    /**
     * The tv Pass
     */
    private TextView tvPass;
    /**
     * The cb Laws
     */
    private CheckBox cbLaws;
    /**
     * The ll QQ
     */
    private LinearLayout llQQ;
    /**
     * The ll Wechat
     */
    private LinearLayout llWechat;
    /**
     * The Tencent
     */
    private Tencent mTencent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        if (!this.isTaskRoot()) {// 判断此activity是不是任务控件的源Activity，“非”也就是说是被系统重新实例化出来的
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        if (GosApplication.flag != 0) {
            GosBaseActivity.noIDAlert(this, R.string.AppID_Toast);
        }

        setContentView(R.layout.activity_gos_user_login);

        // 设置actionBar
        setActionBar(false, false, R.string.app_company);
        initView();
        initEvent();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        JPushInterface.onResume(this);
        autoLogin();

        cleanuserthing();
    }

    private void cleanuserthing() {

        if (isclean) {
            etName.setText("");
            etPsw.setText("");
        }
    }

    private void autoLogin() {

        if (TextUtils.isEmpty(spf.getString("UserName", "")) || TextUtils.isEmpty(spf.getString("PassWord", ""))) {
            return;
        }

        baseHandler.sendEmptyMessageDelayed(handler_key.AUTO_LOGIN.ordinal(), 1000);
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.etName);
        etPsw = (EditText) findViewById(R.id.etPsw);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvForget = (TextView) findViewById(R.id.tvForget);
        tvPass = (TextView) findViewById(R.id.tvPass);
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);

        DotView DotView = (DotView) findViewById(R.id.dotView1);
        llQQ = (LinearLayout) findViewById(R.id.llQQ);
        llWechat = (LinearLayout) findViewById(R.id.llWechat);
        String setTencentAppID = GosDeploy.setTencentAppID();
        String setWechatAppID = GosDeploy.setWechatAppID();
        // 判断腾讯和微信是否需要隐藏和显示
        setWechatOrTencentIsVisable(DotView);
        // 配置文件部署
        btnLogin.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
        btnLogin.setTextColor(GosDeploy.setButtonTextColor());

    }

    protected void setWechatOrTencentIsVisable(DotView DotView) {
        if (!GosDeploy.setWechat()) {

            llWechat.setVisibility(View.GONE);
        }
        if (!GosDeploy.setQQ()) {

            llQQ.setVisibility(View.GONE);
        }

        if (!GosDeploy.setWechat() && !GosDeploy.setQQ()) {
            DotView.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvPass.setOnClickListener(this);

        llQQ.setOnClickListener(this);
        llWechat.setOnClickListener(this);

        cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = etPsw.getText().toString();

                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    // 接着根据requestCode和grantResults(授权结果)做相应的后续处理：
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
            }
        }
    }


    /**
     * 设置云端服务回调
     */
    @SuppressLint("WrongConstant")
    protected void didGetCurrentCloudService(GizWifiErrorCode result,
                                             java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            Toast.makeText(this, toastError(result), toastTime).show();
        }
    }

    /**
     * 用户登录回调
     */
    @Override
    protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {

        progressDialog.cancel();
        Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "User");
        if (GosDeviceListActivity.loginStatus == 4 || GosDeviceListActivity.loginStatus == 3) {
            return;
        }
        Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "UserLogin");

        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {// 登录失败
            Toast.makeText(GosUserLoginActivity.this, toastError(result), toastTime).show();

        } else {// 登录成功

            GosDeviceListActivity.loginStatus = 1;
            Toast.makeText(GosUserLoginActivity.this, R.string.toast_login_successful, toastTime).show();

            if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())
                    && TextUtils.isEmpty(spf.getString("thirdUid", ""))) {
                spf.edit().putString("UserName", etName.getText().toString()).commit();
                spf.edit().putString("PassWord", etPsw.getText().toString()).commit();
            }
            spf.edit().putString("Uid", uid).commit();
            spf.edit().putString("Token", token).commit();

            intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
            intent.putExtra("ThredLogin", true);
            startActivity(intent);
        }
    }

    /**
     * 解绑推送回调
     *
     * @param result
     */
    protected void didChannelIDUnBind(GizWifiErrorCode result) {
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            Toast.makeText(this, toastError(result), toastTime).show();
        }

        Log.i("Apptest", "UnBind:" + result.toString());
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true; // 准备退出
            String doubleClick = (String) getText(R.string.double_click);
            Toast.makeText(this, doubleClick, toastTime).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID,
                                  String eventMessage) {
        super.didNotifyEvent(eventType, eventSource, eventID, eventMessage);

        if (eventID != null && eventID.getResult() != 8316) {
            String toastError = toastError(eventID);

            TipsDialog dia = new TipsDialog(this, toastError);
            dia.show();
        }
    }

    /**
     * 注销函数
     */
    void logoutToClean() {
        spf.edit().putString("UserName", "").commit();
        spf.edit().putString("PassWord", "").commit();
        spf.edit().putString("Uid", "").commit();
        spf.edit().putString("thirdUid", "").commit();

        spf.edit().putString("Token", "").commit();

        if (GosDeviceListActivity.loginStatus == 1) {
            GosDeviceListActivity.loginStatus = 0;
        } else {
            GosDeviceListActivity.loginStatus = 4;
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    public enum handler_key {
        /**
         * 登录
         */
        LOGIN,
        /**
         * 自动登录
         */
        AUTO_LOGIN,
        /**
         * 第三方登录
         */
        THRED_LOGIN,
    }
}
