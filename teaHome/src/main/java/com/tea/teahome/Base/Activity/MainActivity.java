package com.tea.teahome.Base.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

import com.tea.teahome.Base.Widget.MyTabHost;
import com.tea.teahome.R;

/**
 * 实现App底部的三个标签栏
 * 通过点击标签栏进行页面的切换，共有三个页面，分别为
 * KnowledgeHomeActivity.class     进行茶知识的展示
 * TeaHWCTRActivity.class          进行茶盘的控制和数据显示
 * MyInformationActivity.class     进行显示我的用户信息，以及用户登录退出等操作
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-07 20:48
 */
public class MainActivity extends TabActivity {
    public static TabHost tabHost;
    public static Activity activity;

    /**
     * 初始化主页面，布局文件为R.layout.activity_main
     * 初始化底部标签栏，MyTabHost.class
     *
     * @author jiang yuhang
     * @date 2021-02-07 20:49
     **/
    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化页面
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_main);
        //初始化底部标签栏
        new MyTabHost(this, MainActivity.this, getTabHost());
        tabHost = getTabHost();
        activity = this;
    }
}