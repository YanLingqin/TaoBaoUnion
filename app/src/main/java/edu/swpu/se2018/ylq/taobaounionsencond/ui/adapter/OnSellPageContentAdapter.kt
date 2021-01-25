package edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.ItemOnSellPageContentBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.IBaseInfo
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.OnSellContent
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class OnSellPageContentAdapter : RecyclerView.Adapter<OnSellPageContentAdapter.InnerHolder>() {

    private var mItemClickListener: OnOnSellContentItemClickListener?=null
    private val mData =
        arrayListOf<OnSellContent.OnSellContentData.TbkDgOptimusMaterialResponse.ResultList.MapData>()

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemOnSellPageContentBinding = ItemOnSellPageContentBinding.bind(itemView)

        private val tvOnSellOffPrice = itemOnSellPageContentBinding.tvOnSellOffPrice
        private val tvOnSellOriginPrice = itemOnSellPageContentBinding.tvOnSellOriginPrice
        private val tvOnSellTitle = itemOnSellPageContentBinding.tvOnSellTitle
        private val ivOnSellContentCover = itemOnSellPageContentBinding.ivOnSellContentCover


        fun setData(mapData: OnSellContent.OnSellContentData.TbkDgOptimusMaterialResponse.ResultList.MapData) {

            val layoutParams = ivOnSellContentCover.layoutParams
            val height = layoutParams.height
            val width = layoutParams.width
            val coverSize = if (width > height) width / 2 else height / 2
            val pictUrl = UrlUtils.getCoverPath(mapData.pict_url, coverSize)
            //LogUtils.d(this,"pictUrl ---> $pictUrl")
            Glide.with(itemView).load(pictUrl)
                .into(ivOnSellContentCover)
            tvOnSellTitle.text = mapData.title

            tvOnSellOriginPrice.text =
                String.format(
                    itemView.context.getString(R.string.txt_goods_origin_price),
                    mapData.zk_final_price.toFloat()
                )
            tvOnSellOriginPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvOnSellOffPrice.text = String.format(
                itemView.context.getString(R.string.txt_goods_price),
                (mapData.zk_final_price.toFloat() - mapData.coupon_amount)
            )

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_on_sell_page_content,
            parent,
            false
        )
        return InnerHolder(itemView)
    }


    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val mapData = mData[position]
        holder.setData(mapData)
        holder.itemView.setOnClickListener {
            if(mItemClickListener!=null){
                mItemClickListener!!.onSellContentItemClick(mapData)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setData(result: OnSellContent) {
        val mapData = result.data.tbk_dg_optimus_material_response.result_list.map_data
        if (mapData != null) {
            mData.clear()
            mData.addAll(mapData)
            notifyDataSetChanged()
        }

    }

    //加载更多内容
    fun onMoreLoaded(moreResult: OnSellContent) {
        val oldSize = mData.size
        val mapData = moreResult.data.tbk_dg_optimus_material_response.result_list.map_data
        mData.addAll(mapData)
        notifyItemRangeChanged(oldSize -1,mapData.size)
    }

    fun setOnOnSellContentItemClickListener(listener:OnOnSellContentItemClickListener){
        this.mItemClickListener = listener
    }

    interface OnOnSellContentItemClickListener{
        fun onSellContentItemClick(item:IBaseInfo)
    }

}