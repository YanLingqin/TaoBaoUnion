package edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.HomePagerContentDetail
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class LooperPagerAdapter : PagerAdapter() {

    private var mItemClickListener: OnLooperPageItemClickListener? = null
    private val mData = arrayListOf<HomePagerContentDetail>()

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun getDataSize(): Int {
        return mData.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val realPosition = position % mData.size
        val homePagerContentDetail = mData[realPosition]
        val measuredHeight = container.measuredHeight
        val measuredWidth = container.measuredWidth
        //LogUtils.d(this,"measuredHeight -->$measuredHeight")
        //LogUtils.d(this,"measuredWidth -->$measuredWidth")
        val picSize = if (measuredHeight > measuredWidth) measuredHeight / 2 else measuredWidth / 2
        val coverPath = UrlUtils.getCoverPath(homePagerContentDetail.pict_url, picSize)
        //LogUtils.d(this,"coverPath -->$coverPath")
        val iv = ImageView(container.context)
        iv.setOnClickListener {
            if (mItemClickListener != null) {
                val item = mData[realPosition]
                mItemClickListener!!.onLooperItemClick(item)
            }
        }
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        iv.layoutParams = layoutParams
        iv.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(container.context)
            .load(coverPath)
            .placeholder(R.mipmap.ic_launcher)
            .into(iv)
        container.addView(iv)
        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun setData(contents: List<HomePagerContentDetail>) {
        mData.clear()
        mData.addAll(contents)
        notifyDataSetChanged()
    }

    interface OnLooperPageItemClickListener {
        fun onLooperItemClick(item: HomePagerContentDetail)
    }

    fun setOnLooperPageItemClickListener(listener: OnLooperPageItemClickListener) {
        this.mItemClickListener = listener
    }

}