package com.tea.teahome.Knowledge.Bean;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Bean类存储“知识”数据,可通过Collections.sort()方法进行排序
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-10 15:20
 */
public class KnowledgeBean implements Comparable {
    private String title;
    private Drawable icon;
    private String inf;
    private String time;
    private String url;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getInf() {
        return inf;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return int
     * @author jiang yuhang
     * @date 2021-02-17 14:09
     **/
    @Override
    public int compareTo(Object o) {
        KnowledgeBean kb = (KnowledgeBean) o;
        //定义时间戳格式
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Date date1 = null;
        //将字符串类型Time转化为Date类型
        try {
            date = simpleDateFormat.parse(kb.getTime());
            date1 = simpleDateFormat.parse(this.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null && date1 != null) {
            if (date.before(date1)) {
                return -1;
            } else if (date.equals(date1)) {
                return 0;
            } else {
                return 1;
            }
        }
        return 0;
    }
}
