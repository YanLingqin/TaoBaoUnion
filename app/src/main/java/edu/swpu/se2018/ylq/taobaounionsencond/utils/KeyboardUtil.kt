package edu.swpu.se2018.ylq.taobaounionsencond.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object KeyboardUtil {
    fun hide(context:Context,view: View){
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
    }

    fun show(context:Context,view: View){
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view,0)
    }


}