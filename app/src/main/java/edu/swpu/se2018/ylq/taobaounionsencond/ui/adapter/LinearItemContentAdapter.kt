package edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.ItemLinearGoodsContentBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.HomePagerContentDetail
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.ILinearItemInfo
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class LinearItemContentAdapter : RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder>() {
    private var mItemClickListener: OnListenItemClickListener? = null
    private val data = arrayListOf<ILinearItemInfo>()

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemLinearGoodsContentBinding = ItemLinearGoodsContentBinding.bind(itemView)

        fun setData(homePagerContentDetail: ILinearItemInfo) {
            //处理加载的图片，避免浪费资源
            val layoutParams = itemLinearGoodsContentBinding.ivGoodsCover.layoutParams
            val width = layoutParams.width
            val height = layoutParams.height
            //LogUtils.d(this,"cover width ---> $width")
            //LogUtils.d(this,"cover height ---> $height")
            val coverSize = if (width > height) width / 2 else height / 2
            val pictUrl = UrlUtils.getCoverPath(homePagerContentDetail.getCover()!!, coverSize)
            //LogUtils.d(this,"pictUrl ---> $pictUrl")
            Glide.with(itemView.context)
                .load(pictUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(itemLinearGoodsContentBinding.ivGoodsCover)

            val couponAmount = homePagerContentDetail.getCouponAmount()
            itemLinearGoodsContentBinding.tvGoodsOffPrise.text =
                String.format(
                    itemView.context.getString(R.string.txt_goods_off_price),
                    couponAmount
                )
            val startPrice = homePagerContentDetail.getFinalPrice().toFloat()

            itemLinearGoodsContentBinding.tvGoodsTitle.text = homePagerContentDetail.getGoodsTitle()
            itemLinearGoodsContentBinding.tvGoodsAfterOffPrise.text =
                String.format("%.2f", startPrice - couponAmount)
            itemLinearGoodsContentBinding.tvGoodsPrise.text =
                String.format(
                    itemView.context.getString(R.string.txt_goods_price),
                    startPrice
                )
            //为文字设置中划线
            itemLinearGoodsContentBinding.tvGoodsPrise.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemLinearGoodsContentBinding.tvNumberPursue.text =
                String.format(
                    itemView.context.getString(R.string.txt_goods_pursue),
                    homePagerContentDetail.getSales()
                )
        }

    }


    var testCount = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_linear_goods_content, parent, false)
        //LogUtils.d(this,"RecyclerView : onCreateViewHolder .... ${testCount++} ")

        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.setData(data[position])
        //LogUtils.d(this,"RecyclerView : onBindViewHolder .... $position")

        holder.itemView.setOnClickListener {
            if (mItemClickListener != null) {
                mItemClickListener!!.onItemClick(data[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(contents: List<ILinearItemInfo>) {
        data.clear()
        data.addAll(contents)
        notifyDataSetChanged()
    }

    fun addData(contents: List<ILinearItemInfo>) {
        val oldSize = data.size
        data.addAll(contents)
        //更新UI
        notifyItemRangeChanged(oldSize, contents.size)
    }

    interface OnListenItemClickListener {
        fun onItemClick(item: ILinearItemInfo)
    }

    fun setOnListenItemClickListener(listener: OnListenItemClickListener) {
        this.mItemClickListener = listener
    }


}