package com.tea.teahome.Control.View

import android.graphics.Paint

/**
 * @author jiang yuhang
 * @version 1.0
 * @className Ball
 * @program teaHome
 * @date 2021-04-18 19:57
 */
class Ball {
    // 半径
    @kotlin.jvm.JvmField
    var radius = 0

    // 圆心
    @kotlin.jvm.JvmField
    var cx = 0f

    // 圆心
    @kotlin.jvm.JvmField
    var cy = 0f

    // X轴速度
    @kotlin.jvm.JvmField
    var vx = 0f

    // Y轴速度
    @kotlin.jvm.JvmField
    var vy = 0f

    @kotlin.jvm.JvmField
    var paint: Paint? = null

    // 移动
    fun move() {
        //向角度的方向移动，偏移圆心
        cx += vx
        cy += vy
    }

    fun left(): Int {
        return (cx - radius).toInt()
    }

    fun right(): Int {
        return (cx + radius).toInt()
    }

    fun bottom(): Int {
        return (cy + radius).toInt()
    }

    fun top(): Int {
        return (cy - radius).toInt()
    }
}