package edu.swpu.se2018.ylq.taobaounionsencond.base

import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
abstract class BaseActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置这个软件为灰色主题
        //val view = window.decorView
        //val colorMatrix = ColorMatrix()
        //colorMatrix.setSaturation(0f)
        //val paint = Paint()
        //paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        //view.setLayerType(View.LAYER_TYPE_SOFTWARE,paint)
    }


    override fun onDestroy() {
        super.onDestroy()
        this.release()
    }

    //根据子类需要来进行重写
    open fun release() {

    }

}