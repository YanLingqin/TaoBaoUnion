package edu.swpu.se2018.ylq.taobaounionsencond.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.utils.Constants
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TextFlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var mItemClickListener: OnFlowTextItemClickListener? = null
    private var mItemHorizontalSpace = Constants.DEFAULT_SPACE
    private var mItemVerticalSpace = Constants.DEFAULT_SPACE

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout)
        mItemHorizontalSpace = a.getDimension(
            R.styleable.TextFlowLayout_horizontalSpace,
            Constants.DEFAULT_SPACE
        )
        mItemVerticalSpace = a.getDimension(
            R.styleable.TextFlowLayout_verticalSpace,
            Constants.DEFAULT_SPACE
        )
        //LogUtils.d(this, "mItemHorizontalSpace -->$mItemHorizontalSpace")
        //LogUtils.d(this, "mItemVerticalSpace -->$mItemVerticalSpace")
        a.recycle()
    }

    fun getContentSize():Int{
        return mTextList.size
    }


    private var mTextList = ArrayList<String>()

    public fun setTextList(textList: List<String>) {
        removeAllViews()
        mTextList.clear()
        mTextList .addAll(textList)
        mTextList.reverse()
        mTextList.forEach {text ->
            //添加子view
            val item = LayoutInflater.from(context)
                .inflate(R.layout.flow_text_view, this, false) as TextView
            item.text = text
            item.setOnClickListener {
                if (mItemClickListener != null) {
                    mItemClickListener!!.onFlowItemClick(text)
                }
            }
            addView(item)
        }
    }


    //描述多行
    private val lines: ArrayList<List<View>> = ArrayList()
    private var selfWidth: Int = 0
    private var selfHeight: Int = 0
    private var itemHeight: Int = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(childCount==0){
            return
        }
        //描述单行
        var line: ArrayList<View>? = null
        lines.clear()
        selfWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd
        selfHeight = MeasureSpec.getSize(heightMeasureSpec)
        //测量孩子
        val childCount = childCount
        for (i in 0 until childCount) {
            val itemView = getChildAt(i)
            if (itemView.visibility != VISIBLE) {
                //不需要进行测量
                continue
            }
            //测量前
            //LogUtils.d(this, "before height ---> ${itemView.measuredHeight}")
            //LogUtils.d(this, "before width ---> ${itemView.measuredWidth}")
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec)
            //测量后
            //LogUtils.d(this, "after height ---> ${itemView.measuredHeight}")
            //LogUtils.d(this, "after width ---> ${itemView.measuredWidth}")
            if (line == null) {
                line = createNewLine(itemView)
            } else {
                //判断是否能继续添加到该行
                if (canBeAdd(itemView, line)) {
                    //可以添加
                    line.add(itemView)
                } else {
                    //不可以添加  创建新的行
                    line = createNewLine(itemView)
                }
            }
        }
        itemHeight = getChildAt(0).measuredHeight
        val selfHeight =
            lines.size * itemHeight + mItemVerticalSpace * (lines.size + 1)
        //测量自己
        setMeasuredDimension(selfWidth, selfHeight.toInt())
    }


    private fun createNewLine(itemView: View): ArrayList<View> {
        val line = ArrayList<View>()
        line.add(itemView)
        lines.add(line)
        return line
    }

    private fun canBeAdd(itemView: View?, line: List<View>): Boolean {
        //所有已经添加的子VIew宽度相加，(size +1)*mItemHorizontalSpace + itemView.
        //如果小于或者等于当前控件的宽度，则可以添加，否则不能添加
        var totalWidth = itemView!!.measuredWidth
        //遍历子View
        line.forEach {
            totalWidth += it.measuredWidth
        }
        //水平间距的宽度
        totalWidth += (line.size + 1) * mItemHorizontalSpace.toInt()
        //LogUtils.d(this, "totalWidth --->$totalWidth")
        //LogUtils.d(this, "selfWidth --->$selfWidth")
        return totalWidth <= selfWidth
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        LogUtils.d(this, "childCount--->$childCount")
        var topOffset = mItemVerticalSpace
        lines.forEach {
            //每一行
            var leftOffset = mItemHorizontalSpace
            it.forEach { itemView ->
                //每一个item
                itemView.layout(
                    leftOffset.toInt(),
                    topOffset.toInt(),
                    (leftOffset + itemView.measuredWidth).toInt(),
                    (topOffset + itemView.measuredHeight).toInt()
                )
                leftOffset += mItemHorizontalSpace + itemView.measuredWidth
            }
            topOffset += mItemVerticalSpace + itemHeight
        }
    }

    fun setOnFlowTextItemClickListener(listener: OnFlowTextItemClickListener) {
        this.mItemClickListener = listener
    }

    interface OnFlowTextItemClickListener {
        fun onFlowItemClick(text: String)
    }

}