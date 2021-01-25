package edu.swpu.se2018.ylq.taobaounionsencond.utils

import android.content.Context

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SizeUtils {
    companion object{
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}