package com.tea.sql.Bean

import java.util.*

class Knowledge {
    var title: String? = null
    var createTime: Date? = null
    var authorName: String? = null
    var havePic: Boolean = false
    var click: Int = 0
    val url: String
        get() = "$title+$authorName+$createTime.html"
    val picUrl: String
        get() = "$title+$authorName+$createTime.png"
}