package edu.swpu.se2018.ylq.taobaounionsencond.utils

import android.util.Log

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2020/12/12
 *     desc   : log控制工具类
 *     version: 1.0
 * </pre>
 */
object LogUtils {
    private var currentLev = 4
    private const val DEBUG_LEV = 4
    private const val INFO_LEV = 3
    private const val WARNING_LEV = 2
    private const val ERROR_LEV = 1

    fun d(clazz: Any, msg: String) {
        if (currentLev >= DEBUG_LEV) {
            Log.d(clazz.javaClass.simpleName, " $msg ")
        }
    }

    fun w(clazz: Any, msg: String) {
        if (currentLev >= WARNING_LEV) {
            Log.w(clazz.javaClass.simpleName, " $msg ")
        }
    }

    fun i(clazz: Any, msg: String) {
        if (currentLev >= INFO_LEV) {
            Log.i(clazz.javaClass.simpleName, " $msg ")
        }
    }

    fun e(clazz: Any, msg: String) {
        if (currentLev >= ERROR_LEV) {
            Log.e(clazz.javaClass.simpleName, " $msg ")
        }
    }
}