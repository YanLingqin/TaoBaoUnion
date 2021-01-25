package com.lcodecore.tkrefreshlayout.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView


/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TbNestedScrollView : NestedScrollView {

    private val TAG= "TbNestedScrollView"
    private lateinit var mRecyclerView: RecyclerView
    private var originScroll: Int = 0
    private var mLooperHeaderHeight: Int = 0

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(
        context: Context, attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        //Log.d(TAG, "onNestedScroll......")
        if(target is RecyclerView ){
            this.mRecyclerView = target
        }
    }

    override fun onNestedPreScroll(
        target: View,
        dx: Int,  //x轴滑动的差值
        dy: Int,  // y轴的滑动差值
        consumed: IntArray,
        type: Int
    ) {
        //LogUtils.d(this,"dx -->$dx")
        //LogUtils.d(this,"dy -->$dy")
        if(originScroll<mLooperHeaderHeight){
            scrollBy(dx,dy)
            consumed[0] = dx
            consumed[1] = dy
            //这部分的滑动被消费了，RecyclerView是接收不到的
        }
        //上面部分滑动完成后，该RecyclerView滑动了，这时就向下传递事件
        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        this.originScroll = t
        //LogUtils.d(this,"vertical --> $t")
        super.onScrollChanged(l, t, oldl, oldt)
    }

    //获得NestedScrollView中除去RecyclerView部分的高度
    fun setLooperHeaderHeight(looperHeaderHeight:Int){
        this.mLooperHeaderHeight = looperHeaderHeight
    }

    //判断子类是否滑动到了底部
    fun isInBottom():Boolean{
        if(this::mRecyclerView.isInitialized){
            //判断是否能滑动来让决定事件是否被拦截
            //Log.d(TAG, "isInBottom: isBottom --->$isBottom ")
            return !mRecyclerView.canScrollVertically(1)
        }
        return false
    }

}