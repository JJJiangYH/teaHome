package com.tea.sql.Bean

import java.text.SimpleDateFormat
import java.util.*

class Knowledge {
    var title: String? = null
    private var createTime: Date? = null
    var authorName: String? = null
    var havePic: Boolean? = null
    var click: Int? = null

    fun getCreateTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(createTime)
    }

    fun setCreateTime(createTime: Date?) {
        this.createTime = createTime
    }

    val url: String
        get() = title + "+" + authorName + "+" + getCreateTime() + ".html"

    override fun toString(): String {
        return url
    }
}