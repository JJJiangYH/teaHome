package com.tea.teahome.Knowledge.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tea.teahome.Knowledge.Bean.KnowledgeBean;
import com.tea.teahome.R;
import com.tea.teahome.Utils.ViewUtil;

import java.util.ArrayList;

/**
 * 知识数据适配器
 *
 * @version 1.0
 * @className: KnowledgeAdapter
 * @author: jiang yuhang
 * @create: 2021-02-09 13:28
 */
public class KnowledgeAdapter extends BaseAdapter {
    private final ArrayList<KnowledgeBean> knowledgeList;
    private final Context context;

    /**
     * 类的初始化方法
     *
     * @author jiang yuhang
     * @date 2021-02-09 13:28
     **/
    public KnowledgeAdapter(Context context, ArrayList<KnowledgeBean> knowledgeList) {
        this.knowledgeList = knowledgeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return knowledgeList.size();
    }

    @Override
    public Object getItem(int i) {
        return knowledgeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * @return android.view.View
     * @author jiang yuhang
     * @date 2021-02-10 10:01
     **/
    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //初始化变量
        View view;
        //通过context获取系统服务得到一个LayoutInflater
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //获取position位置条目对应的list集合中的新闻数据，Bean对象
        KnowledgeBean knowledgeBean = (KnowledgeBean) this.getItem(position);

        //如果icon不为空则创建knowledge_item布局，否则创建knowledge_item_without_image布局
        if (knowledgeBean.getIcon() != null) {
            //通过LayoutInflater将一个布局转换为view对象
            view = layoutInflater.inflate(R.layout.item_knowledge, null);
            ViewUtil.setIcon(view, knowledgeBean.getIcon(), R.id.item_iv_image);
        } else {
            //通过LayoutInflater将一个布局转换为view对象
            view = layoutInflater.inflate(R.layout.item_knowledge_without_image, null);
        }

        //将数据设置给这些子控件做显示
        ViewUtil.setTitle(view, knowledgeBean.getTitle(), R.id.item_tv_title);
        ViewUtil.setTime(view, knowledgeBean.getTime(), R.id.item_tv_time);
        ViewUtil.setInf(view, knowledgeBean.getInf(), R.id.item_tv_inf);

        return view;
    }
}
