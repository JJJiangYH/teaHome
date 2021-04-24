package com.tea.view.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

/**
 * 继承TextView，创建一个页面上方的状态栏遮挡
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-08 23:28
 */
@SuppressLint("ViewConstructor")
public class StatusBar extends androidx.appcompat.widget.AppCompatTextView {
    /**
     * 初始化方法
     *
     * @param color   TopBar颜色
     * @param context 添加topBar的窗口
     * @author jiang yuhang
     * @date 2021-02-08 23:28
     **/
    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    public StatusBar(Context context, int color) {
        super(context);
        //设置背景颜色
        this.setBackgroundDrawable(getResources().getDrawable(color));
        //设置遮挡物高度
        this.setHeight(getStatusBarHeight());
        //设置遮挡物宽度
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setTag("StatusBar");
    }

    /**
     * 获取状态栏高度
     *
     * @return int
     * @author jiang yuhang
     * @date 2021-02-08 23:48
     **/
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
