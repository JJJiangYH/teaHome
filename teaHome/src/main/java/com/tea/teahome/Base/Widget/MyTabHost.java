package com.tea.teahome.Base.Widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.tea.teahome.Control.activity.ControlActivity;
import com.tea.teahome.Knowledge.Activity.KnowledgeHomeActivity;
import com.tea.teahome.R;
import com.tea.teahome.User.Activity.MyInformationActivity;

/**
 * 自定义的TabHost，用于主页面下方的标签栏
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-13 23:16
 */
public class MyTabHost implements View.OnClickListener {
    //声明三个标签的标识串
    private static final String FIRST_TAG = "first";
    private static final String SECOND_TAG = "second";
    private static final String THIRD_TAG = "third";

    private final Bundle mBundle = new Bundle();//声明一个包裹对象
    private final TabHost tabHost;//声明一个标签栏对象
    private final Context context;//声明一个上下文对象
    private final Activity activity;
    private LinearLayout ll_first, ll_second, ll_third;

    /**
     * 初始化方法变量
     *
     * @author jiang yuhang
     * @date 2021-02-13 23:16
     **/
    public MyTabHost(Context context, Activity activity, TabHost tabHost) {
        //初始化变量
        this.context = context;
        this.tabHost = tabHost;
        this.activity = activity;
        //初始化TabHost
        initTabHost();
    }

    /**
     * 给TabHost添加图像、监听、标签
     *
     * @author jiang yuhang
     * @date 2021-02-13 23:48
     **/
    private void initTabHost() {
        //为LL设置监听器
        ll_first = ll_setOnClickListener(R.id.ll_first);
        ll_second = ll_setOnClickListener(R.id.ll_second);
        ll_third = ll_setOnClickListener(R.id.ll_third);

        //向Tab标签栏中添加图像
        addDrawableToTextView(R.drawable.tab_first_selector, R.id.tab_tv_first);
        addDrawableToTextView(R.drawable.tab_second_selector, R.id.tab_tv_second);
        addDrawableToTextView(R.drawable.tab_third_selector, R.id.tab_tv_third);

        //向标签栏添加第一个标签，其中页面视图显示TeaKnowledgeActivity.class
        this.tabHost.addTab(getNewTab(FIRST_TAG, R.string.tab_knowledge,
                KnowledgeHomeActivity.class));
        //向标签栏添加第二个标签，其中页面视图显示TeaControlActivity.class
        this.tabHost.addTab(getNewTab(SECOND_TAG, R.string.tab_control,
                ControlActivity.class));
        //向标签栏添加第三个标签，其中页面视图显示MyInfActivity.class
        this.tabHost.addTab(getNewTab(THIRD_TAG, R.string.tab_myInf,
                MyInformationActivity.class));

        //默认显示第二个标签的内容视图
        changeContainerView(ll_second);
    }

    /**
     * 给标签注册监听
     *
     * @return android.widget.LinearLayout
     * @author jiang yuhang
     * @date 2021-02-13 23:46
     **/
    private LinearLayout ll_setOnClickListener(int ll) {
        LinearLayout temp = activity.findViewById(ll);
        temp.setOnClickListener(this);
        return temp;
    }

    /**
     * 将drawableInt加入textViewInt所对应的TextView中
     *
     * @author jiang yuhang
     * @date 2021-02-13 23:44
     **/
    @SuppressLint("UseCompatLoadingForDrawables")
    private void addDrawableToTextView(int drawableInt, int textViewInt) {
        Drawable drawable = context.getResources().getDrawable(drawableInt);
        drawable.setBounds(0, 0, 100, 100);
        TextView textView = activity.findViewById(textViewInt);
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 切换TabHost的标签页面
     *
     * @author jiang yuhang
     * @date 2021-02-14 00:10
     **/
    @SuppressLint("ResourceAsColor")
    private void changeContainerView(View v) {
        //取消选中所有标签
        ll_first.setSelected(false);
        ll_second.setSelected(false);
        ll_third.setSelected(false);

        v.setSelected(true);//选中指定的标签View

        if (v == ll_first) {
            tabHost.setCurrentTabByTag(FIRST_TAG); // 设置当前标签为第一个标签
        } else if (v == ll_second) {
            tabHost.setCurrentTabByTag(SECOND_TAG); // 设置当前标签为第二个标签
        } else if (v == ll_third) {
            tabHost.setCurrentTabByTag(THIRD_TAG); // 设置当前标签为第三个标签
        }
    }

    /**
     * 根据定制的参数获得新的标签样式
     *
     * @return android.widget.TabHost.TabSpec
     * @author jiang yuhang
     * @date 2021-02-13 00:07
     **/
    @SuppressLint("UseCompatLoadingForDrawables")
    private android.widget.TabHost.TabSpec getNewTab(String spec, int label, Class<?> cls) {
        //创建一个意图，并存入指定包裹
        Intent intent = new Intent(context, cls).putExtras(mBundle);
        //返回生成的新的标签样式
        return tabHost.newTabSpec(spec).setContent(intent)
                .setIndicator(context.getString(label), context.getResources().getDrawable(R.drawable.selector_tabhost_background));
    }

    /**
     * 标签页面监听的响应函数
     *
     * @author jiang yuhang
     * @date 2021-02-14 00:15
     **/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_first ||
                v.getId() == R.id.ll_second ||
                v.getId() == R.id.ll_third) {
            changeContainerView(v); //点击哪个标签，就切换到改标签对应的内容视图
        }
    }
}
