package edu.swpu.se2018.ylq.taobaounionsencond.ui.custom


import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mDegrees = 30f
    private var mNeedRotate = true

    init {
        setImageResource(R.mipmap.loading)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LogUtils.d(this,"onAttachedToWindow")
        startRotate()
    }

    private fun startRotate() {
        mNeedRotate = true
        post(object : Runnable {
            override fun run() {
                mDegrees += 10
                if (mDegrees >= 360f) {
                    mDegrees = 0f
                }
                invalidate()
                //LogUtils.d(this@LoadingView,"loading...... ")
                //判断是否要继续旋转
                if (visibility != VISIBLE && !mNeedRotate) {
                    removeCallbacks(this)
                }else{
                    postDelayed(this, 10)
                }
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        LogUtils.d(this,"onDetachedFromWindow")
        stopRotate()
    }

    private fun stopRotate() {
        mNeedRotate = false
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.rotate(mDegrees, width / 2.toFloat(), height / 2.toFloat())
        super.onDraw(canvas)
    }
}