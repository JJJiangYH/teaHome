package com.tea.teahome.Knowledge.Activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tea.markdown.Activity.MarkdownActivity;
import com.tea.teahome.Base.Activity.MainActivity;
import com.tea.teahome.Knowledge.Adapter.KnowledgeAdapter;
import com.tea.teahome.Knowledge.Bean.KnowledgeBean;
import com.tea.teahome.Knowledge.Utils.KnowledgeDataUtils;
import com.tea.teahome.Knowledge.Utils.KnowledgeFTPUtils;
import com.tea.teahome.R;
import com.tea.view.View.Toast;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;
import static com.tea.view.Utils.ViewUtil.addStatusBar;

/**
 * 知识库页面
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-07 20:48
 */
public class KnowledgeHomeActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {
    /**
     * 滑动刷新Layout
     */
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 知识显示View
     */
    @BindView(R.id.lv_knowledge)
    ListView lv_news;
    /**
     * 提示Handler
     */
    private final Handler handler = new Handler(msg -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            update_lv_news();
        }
        if (msg.what == 0) {
            Toast.getToast(KnowledgeHomeActivity.this, "刷新完成").show();
            return true;
        } else {
            Toast.getToast(KnowledgeHomeActivity.this, "服务器连接失败，请检查网络后重试").show();
            return false;
        }
    });

    /**
     * 获取全部的知识数据
     * 通过时间对知识排序
     * 并以ArrayList<KnowledgeBean>形式返回
     *
     * @return java.util.ArrayList<com.tea.teahome.Knowledge.Bean.KnowledgeBean> 全部的知识数据
     * @author jiang yuhang
     * @date 2021-02-16 17:58
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    public List<KnowledgeBean> getAllKnowledge() {
        String dir = this.getFilesDir().getPath();//定义文件夹名称
        //存储所有知识数据类
        KnowledgeDataUtils knowledgeDataUtils = new KnowledgeDataUtils(dir);
        //获得一个存储所有知识数据ArrayList
        List<KnowledgeBean> knowledgeBeanArrayList = knowledgeDataUtils.getKnowledgeBeanList();
        //通过时间进行排序
        Collections.sort(knowledgeBeanArrayList);
        //返回一个存储所有知识数据的ArrayList
        return knowledgeBeanArrayList;
    }

    /**
     * @author jiang yuhang
     * @date 2021-02-07 20:52
     **/
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //初始化页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_home);
        ButterKnife.bind(this);
        //添加状态栏遮挡
        addStatusBar(this, R.id.ll_knowledge, R.color.statusBar_color);
        //初始化控件
        initView();
    }

    /**
     * 获取数据填入adapter，填充ListView
     *
     * @author jiang yuhang
     * @date 2021-02-07 21:01
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initView() {
        //更新lv_news
        update_lv_news();
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 更新Lv_news
     *
     * @author jiang yuhang
     * @date 2021-02-19 20:41
     **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void update_lv_news() {
        //找到控件
        List<KnowledgeBean> allNews = getAllKnowledge();
        KnowledgeAdapter knowledgeAdapter =
                new KnowledgeAdapter(KnowledgeHomeActivity.this, allNews);
        //创建一个adapter设置给listView
        lv_news.setAdapter(knowledgeAdapter);
    }

    /**
     * listView的条目点击时会调用该方法
     *
     * @param parent   代表listView
     * @param view     点击的条目多对应的view对象
     * @param position 条目的位置
     * @param id       条目的id
     * @author jiang yuhang
     * @date 2021-02-07 21:20
     **/
    @Override
    @OnItemClick(R.id.lv_knowledge)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //需要获取条目上bean对象中数据
        KnowledgeBean bean = (KnowledgeBean) parent.getItemAtPosition(position);
        //跳转新的Class，并传递数据
        Intent intent = new Intent(MainActivity.activity, ShowKnowledgeActivity.class);
        Bundle bundle = new Bundle();//建立一个传递知识的包裹
        ActivityOptions options = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = makeSceneTransitionAnimation(MainActivity.activity,
                    Pair.create(view, "web"));
        }
        //将数据装入包裹
        bundle.putString("title", bean.getTitle());
        bundle.putString("inf", bean.getInf());
        bundle.putString("time", bean.getTime());
        bundle.putString("url", bean.getUrl());
        //将包裹塞入意图
        intent.putExtras(bundle);
        //向知识详情页传递bundle
        ActivityCompat.startActivity(MainActivity.activity, intent, options.toBundle());
    }

    @Override
    public void onRefresh() {
        ExecutorService single = Executors.newSingleThreadExecutor();
        single.execute(
                () -> {
                    Message message = new Message();
                    message.what = 1;//初始值为1，0为下载成功，1为下载失败
                    //从FTP中下载文件
                    File file = new File(getFilesDir().getPath());
                    for (File listFile : file.listFiles()) {
                        listFile.delete();
                    }
                    KnowledgeFTPUtils knowledge = new KnowledgeFTPUtils(
                            getFilesDir().getPath());
                    //判断登录是否登陆成功，成功就下载所有文件，否则提示网络连接失败
                    if (knowledge.isConnected()) {
                        knowledge.downloadFilesFromFtp();
                        message.what = 0;
                    } else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(message);
                    swipeRefreshLayout.setRefreshing(false);
                }
        );
        single.shutdown();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @OnClick(R.id.edit)
    @Override
    public void onClick(View v) {
        if (v.getTag().equals("edit")) {
            Intent intent = new Intent(this, MarkdownActivity.class);
            startActivity(intent);
        }
    }
}