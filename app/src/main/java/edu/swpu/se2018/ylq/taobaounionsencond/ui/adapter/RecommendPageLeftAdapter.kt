package edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.ItemRecommendCategoryBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendPageCategory
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendPageCategoryData
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendPageLeftAdapter : RecyclerView.Adapter<RecommendPageLeftAdapter.InnerHolder>() {

    private lateinit var mOnLeftItemClickListener: OnLeftItemClickListener
    private val mData = arrayListOf<RecommendPageCategoryData>()
    private var mCurrentRecommendPosition = 0

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recommend_category, parent, false)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemTv = holder.itemView.findViewById<TextView>(R.id.tv_left_category)
        val recommendPageCategoryData = mData[position]
        if (position == mCurrentRecommendPosition) {
            itemTv.setBackgroundColor(
                ResourcesCompat.getColor(
                    itemTv.resources,
                    R.color.colorEFEEEE,
                    null
                )
            )
        } else {
            itemTv.setBackgroundColor(
                ResourcesCompat.getColor(
                    itemTv.resources,
                    R.color.white,
                    null
                )
            )
        }
        itemTv.text = recommendPageCategoryData.favorites_title
        itemTv.setOnClickListener {
            if(this::mOnLeftItemClickListener.isInitialized && mCurrentRecommendPosition!=position){
                mOnLeftItemClickListener.itemClick(recommendPageCategoryData)
                mCurrentRecommendPosition = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(categories: RecommendPageCategory) {
        val data = categories.data
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
        if(mData.size>0){
            mOnLeftItemClickListener.itemClick(mData[0])
        }
    }

    fun setOnLeftItemClickListener(listener: OnLeftItemClickListener) {
        this.mOnLeftItemClickListener = listener
    }

    interface OnLeftItemClickListener {
        fun itemClick(item: RecommendPageCategoryData)
    }
}