package edu.swpu.se2018.ylq.taobaounionsencond.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import edu.swpu.se2018.ylq.taobaounionsencond.R

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/04
 *     desc   :  实现自动轮播图
 *     version: 1.0
 * </pre>
 */
class AutoLoopViewPager : ViewPager {

    //默认切换时长
    val DEFAULT_DURATION = 3000L

    private var mDuration = DEFAULT_DURATION

    constructor(context: Context) : this(context,null) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttribute(attrs)
    }

    private fun initAttribute(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopViewPager)
        mDuration = a.getInteger(R.styleable.AutoLoopViewPager_duration, DEFAULT_DURATION.toInt()).toLong()
        a.recycle()
    }

    //设置切换间隔时长
    fun setDuration(duration: Long){
        this.mDuration = duration
    }


    private var isLoop = false

    fun startLoop() {
        isLoop = true
        //先获得当前位置
        post(mTask)
    }
    private val mTask =object :Runnable {
        override fun run() {
            var currentItem = currentItem
            currentItem++
            setCurrentItem(currentItem)
            if (isLoop) {
                postDelayed(this, mDuration)
            }
        }
    }
    fun stopLoop() {
        removeCallbacks(mTask)
        isLoop = false
    }
}