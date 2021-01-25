package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

import android.text.TextUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class SearchResult(
    val code: Long,
    val `data`: Data,
    val message: String,
    val success: Boolean
){
    data class Data(
        val tbk_dg_material_optional_response: TbkDgMaterialOptionalResponse
    ){
        data class TbkDgMaterialOptionalResponse(
            val request_id: String,
            val result_list: ResultList,
            val total_results: Long
        ){
            data class ResultList(
                val map_data: List<MapData>
            ){
                data class MapData(
                    val category_id: Long,
                    val category_name: String,
                    val commission_rate: String,
                    val commission_type: String,
                    val coupon_amount: String,
                    val coupon_end_time: String,
                    val coupon_id: String,
                    val coupon_info: String,
                    val coupon_remain_count: Long,
                    val coupon_share_url: String,
                    val coupon_start_fee: String,
                    val coupon_start_time: String,
                    val coupon_total_count: Long,
                    val include_dxjh: String,
                    val include_mkt: String,
                    val info_dxjh: String,
                    val item_description: String,
                    val item_id: Long,
                    val item_url: String,
                    val jdd_num: Long,
                    val jdd_price: Any,
                    val level_one_category_id: Long,
                    val level_one_category_name: String,
                    val nick: String,
                    val num_iid: Long,
                    val oetime: Any,
                    val ostime: Any,
                    val pict_url: String,
                    val presale_deposit: String,
                    val presale_end_time: Long,
                    val presale_start_time: Long,
                    val presale_tail_end_time: Long,
                    val presale_tail_start_time: Long,
                    val provcity: String,
                    val real_post_fee: String,
                    val reserve_price: String,
                    val seller_id: Long,
                    val shop_dsr: Long,
                    val shop_title: String,
                    val short_title: String,
                    val small_images: SmallImages,
                    val title: String,
                    val tk_total_commi: String,
                    val tk_total_sales: String,
                    val url: String,
                    val user_type: Long,
                    val volume: Long,
                    val white_image: String,
                    val x_id: String,
                    val zk_final_price: String
                ):ILinearItemInfo{

                    data class SmallImages(
                        val string: List<String>
                    )

                    override fun getFinalPrice(): String {
                        return zk_final_price
                    }

                    override fun getCouponAmount(): Long {
                        return if(TextUtils.isEmpty(coupon_amount)){
                            0L
                        }else{
                            coupon_amount.toLong()
                        }
                    }

                    override fun getSales(): Long {
                        return volume
                    }

                    override fun getCover(): String {
                        return pict_url
                    }

                    override fun getGoodsTitle(): String {
                        return title
                    }

                    override fun getGoodsUrl(): String {
                       return url
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "SearchResult(code=$code, `data`=$`data`, message='$message', success=$success)"
    }
}



