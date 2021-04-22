package com.tea.sql.Bean

import java.util.*

class Knowledge {
    var title: String? = null
    var createTime: Date? = null
    var authorName: String? = null
    val url: String
        get() = "$title+$authorName+$createTime"

    override fun toString(): String {
        return url
    }
}