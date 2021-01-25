package edu.swpu.se2018.ylq.taobaounionsencond.utils

import android.widget.Toast
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseApplication

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/03
 *     desc   : 保证Toast多次点击只弹出一次
 *     version: 1.0
 * </pre>
 */
object ToastUtil {

    private var toast: Toast? = null
    fun showToast(tips: String) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(tips)
        }
        toast!!.show()
        toast = null
    }

}