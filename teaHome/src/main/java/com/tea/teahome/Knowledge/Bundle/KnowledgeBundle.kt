package com.tea.teahome.Knowledge.Bundle;

import android.app.Activity;
import android.os.Bundle;

/**
 * 自定义的知识数据包裹
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-12 19：10
 */
public class KnowledgeBundle {
    private final Bundle bundle;

    //初始化方法
    public KnowledgeBundle(Activity activity) {
        this.bundle = getBundle(activity);
    }

    //获取intent传输的包裹
    public Bundle getBundle(Activity activity) {
        return activity.getIntent().getExtras();
    }

    //获取包裹中的title
    public String getTitleFromBundle() {
        return bundle.getString("title");
    }

    public String getWebUrlFromBundle() {
        return bundle.getString("url");
    }

    //获取包裹中的inf
    public String getInformationFromBundle() {
        return bundle.getString("inf");
    }

    //获取包裹中的time
    public String getTimeFromBundle() {
        return bundle.getString("time");
    }
}
