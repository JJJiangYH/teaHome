package com.tea.teahome.Knowledge.Bundle

import android.app.Activity
import android.os.Bundle

/**
 * 自定义的知识数据包裹
 *
 * @author : jiang yuhang
 * @version 1.0
 * @date : 2021-02-12 19：10
 */
class KnowledgeBundle(activity: Activity) {
    private val bundle: Bundle? = activity.intent.extras

    //获取包裹中的title
    val titleFromBundle: String?
        get() = bundle!!.getString("title")
    val webUrlFromBundle: String?
        get() = bundle!!.getString("url")

    //获取包裹中的inf
    val informationFromBundle: String?
        get() = bundle!!.getString("inf")

    //获取包裹中的time
    val timeFromBundle: String?
        get() = bundle!!.getString("time")

}