package com.tea.ftp.bean

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Bean类存储“知识”数据,可通过Collections.sort()方法进行排序
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-10 15:20
 */
class KnowledgeBean : Comparable<Any?> {
    var title: String? = null
    var icon: Drawable? = null
    var inf: String? = null
    var time: String? = null
    var url: String? = null
    var click: Int = 0

    /**
     * @return int
     * @author jiang yuhang
     * @date 2021-02-17 14:09
     */
    override fun compareTo(o: Any?): Int {
        val kb = o as KnowledgeBean?
        //定义时间戳格式
        @SuppressLint("SimpleDateFormat")
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        var date1: Date? = null
        //将字符串类型Time转化为Date类型
        try {
            date = simpleDateFormat.parse(kb!!.time)
            date1 = simpleDateFormat.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return if (date != null && date1 != null) {
            when {
                date.before(date1) -> {
                    -1
                }
                date == date1 -> {
                    0
                }
                else -> {
                    1
                }
            }
        } else 0
    }
}