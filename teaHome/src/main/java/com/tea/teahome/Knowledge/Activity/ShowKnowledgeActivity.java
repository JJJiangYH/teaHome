package com.tea.teahome.Knowledge.Activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tea.teahome.Knowledge.Bundle.KnowledgeBundle;
import com.tea.teahome.R;
import com.tea.view.Utils.ViewUtil;

import static com.tea.view.Utils.ViewUtil.addStatusBar;

/**
 * 初始化“知识详情”界面，布局文件为R.layout.activity_show_knowledge
 *
 * @version 1.0
 * @className: ShowKnowledgeActivity
 * @author: jiang yuhang
 * @date 2021-02-16 17:30
 */
public class ShowKnowledgeActivity extends AppCompatActivity {
    KnowledgeBundle kb;

    /**
     * 接受Intent传来的Bundle，并对页面中的组件文字进行设置
     *
     * @author jiang yuhang
     * @date 2021-02-09 22:40
     **/
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_knowledge);
        //接受Intent传来的Bundle
        kb = new KnowledgeBundle(this);
        String path = "file:///" + getFilesDir().getPath() + "/" + kb.getWebUrlFromBundle();
        //添加状态栏遮挡
        addStatusBar(this, R.id.LinearLayout_knowledge_context, R.color.topBar_color);
        //定义背景颜色，设置WebView的背景颜色
        this.findViewById(R.id.content_knowledge).setBackgroundColor(getColor(R.color.app_bg_color));
        //设置标题、内容
        ViewUtil.setTitle(this, kb.getTitleFromBundle(), R.id.topBar_knowledge_show);
        ViewUtil.setWeb(this, path, R.id.content_knowledge);
        ViewUtil.setInf(this, "来源：" + kb.getInformationFromBundle(), R.id.inf_knowledge);
        ViewUtil.setTime(this, "时间：" + kb.getTimeFromBundle(), R.id.time_knowledge);
    }
}