package edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.ItemRecommendContentBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.MapData
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendContent
import edu.swpu.se2018.ylq.taobaounionsencond.utils.Constants
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendPageContentAdapter :
    RecyclerView.Adapter<RecommendPageContentAdapter.InnerHolder>() {

    private var mRecommendPageContentItemClick: OnRecommendPageContentItemClick? = null
    private var mData = arrayListOf<MapData>()

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemRecommendContentBinding = ItemRecommendContentBinding.bind(itemView)
        val ivRecommendCover = itemRecommendContentBinding.ivRecommendCover
        private val tvRecommendTitle = itemRecommendContentBinding.tvRecommendTitle
        val tvRecommendBuy = itemRecommendContentBinding.tvRecommendBuy
        val tvRecommendOffPrice = itemRecommendContentBinding.tvRecommendOffPrice
        private val tvRecommendOriginalPrice = itemRecommendContentBinding.tvRecommendOriginalPrice


        fun setData(mapData: MapData) {
            tvRecommendTitle.text = mapData.title
            LogUtils.d(this, "tvRecommendTitle ---> ${mapData.title}")


            LogUtils.d(this, "tvRecommendOriginalPrice ---> ${mapData.zk_final_price}")
            val coverPath = UrlUtils.getCoverPath(mapData.pict_url)
            LogUtils.d(this, "coverPath ---> ${coverPath}")
            Glide.with(itemView.context).load(coverPath)
                    .into(ivRecommendCover)


            if (TextUtils.isEmpty(mapData.coupon_click_url)) {
                tvRecommendOriginalPrice.text = "晚了，没有优惠券了下次早点哟"
                tvRecommendBuy.visibility = View.GONE
            } else {
                tvRecommendOriginalPrice.text = String.format(
                    itemView.context.getString(R.string.txt_goods_origin_price),
                    mapData.zk_final_price.toFloat()
                )
            }
            if (TextUtils.isEmpty(mapData.coupon_info)) {
                tvRecommendOffPrice.visibility = View.GONE
            } else {
                tvRecommendOffPrice.text = mapData.coupon_info
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recommend_content, parent, false)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.setData(mData[position])
        LogUtils.d(this,"position ----> $position")
        holder.itemView.setOnClickListener {
            if (mRecommendPageContentItemClick != null) {
                mRecommendPageContentItemClick!!.onContentItemClick(mData[position])
            }
        }
    }

    override fun getItemCount(): Int {
        LogUtils.d(this,"mdata size ---> ${mData.size}")
        return mData.size
    }

    fun setData(content: RecommendContent) {
        if (content.code == Constants.SUCCESS_CODE) {
            val uatmTbkItem =
                content.data.tbk_dg_optimus_material_response.result_list.map_data
            this.mData.clear()
            mData.addAll(uatmTbkItem)
            notifyDataSetChanged()
        }
    }

    fun setOnRecommendPageContentItemClick(listener: OnRecommendPageContentItemClick) {
        this.mRecommendPageContentItemClick = listener
    }

    interface OnRecommendPageContentItemClick {
        fun onContentItemClick(item: MapData)
    }


}