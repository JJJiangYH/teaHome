package com.tea.teahome.Control.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fujiayi on 2017/6/20.
 */
public abstract class ActivityCommon extends AppCompatActivity {
    public static final int GONE = View.GONE;
    public static final int VISIBLE = View.VISIBLE;

    public static final String MIN_TIME = "分钟";
    public static final String HOUR_TIME = "小时";

    public static final String OPEN = "OPEN";
    public static final String CLOSE = "CLOSE";
    public static final String SET = "SET";

    public static final String TIMER = "TIMER";
    public static final String TEMP = "TEMP";

    /**
     * 动词
     */
    public static final HashMap<String, String> verb = new HashMap<>();
    /**
     * 名词
     */
    public static final HashMap<String, String> noun = new HashMap<>();
    public static int SEEKBAR_MAX = 60;
    public static String TEMP_UNIT;
    public static int TEMP_NOW = 100;

    static {
        verb.put("打开", OPEN);
        verb.put("开启", OPEN);
        verb.put("启动", OPEN);
        verb.put("进行", OPEN);
        verb.put("使用", OPEN);

        verb.put("设置", SET);
        verb.put("更改", SET);
        verb.put("调整", SET);
        verb.put("修改", SET);
        verb.put("设定", SET);

        verb.put("关闭", CLOSE);
        verb.put("取消", CLOSE);

        noun.put("定时", TIMER);
        noun.put("计时", TIMER);

        noun.put("恒温", TEMP);
        noun.put("温度", TEMP);
        noun.put("保温", TEMP);
    }

    public boolean isTimerRunning = false;
    public boolean isTempUnitC = true;
    protected String nluResult;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (!msg.obj.equals(""))
                nluResult = msg.obj.toString();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            requestPermissions(toApplyList.toArray(tmpList), 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
