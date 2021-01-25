package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

import android.text.TextUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class OnSellContent(
    val code: Int,
    val `data`: OnSellContentData,
    val message: String,
    val success: Boolean
){
    data class OnSellContentData(
        val tbk_dg_optimus_material_response: TbkDgOptimusMaterialResponse
    ){
        data class TbkDgOptimusMaterialResponse(
            val is_default: String,
            val request_id: String,
            val result_list: ResultList
        ){
            data class ResultList(
                val map_data: List<MapData>
            ){
                data class MapData(
                    val category_id: Int,
                    val category_name: Any,
                    val click_url: String,
                    val commission_rate: String,
                    val coupon_amount: Int,
                    val coupon_click_url: String,
                    val coupon_end_time: String,
                    val coupon_info: Any,
                    val coupon_remain_count: Int,
                    val coupon_share_url: String,
                    val coupon_start_fee: String,
                    val coupon_start_time: String,
                    val coupon_total_count: Int,
                    val item_description: String,
                    val item_id: Long,
                    val level_one_category_id: Int,
                    val level_one_category_name: String,
                    val nick: String,
                    val pict_url: String,
                    val seller_id: Long,
                    val shop_title: Any,
                    val small_images: SmallImages,
                    val title: String,
                    val user_type: Int,
                    val volume: Int,
                    val zk_final_price: String
                ):IBaseInfo{

                    data class SmallImages(
                        val string: List<String>
                    )

                    override fun getCover(): String {
                       return pict_url
                    }

                    override fun getGoodsTitle(): String {
                        return title
                    }

                    override fun getGoodsUrl(): String {
                        return if (TextUtils.isEmpty(coupon_click_url)) {
                            coupon_click_url
                        }else{
                            click_url
                        }
                    }

                    override fun toString(): String {
                        return "MapData(category_id=$category_id, category_name=$category_name, click_url='$click_url', commission_rate='$commission_rate', coupon_amount=$coupon_amount, coupon_click_url='$coupon_click_url', coupon_end_time='$coupon_end_time', coupon_info=$coupon_info, coupon_remain_count=$coupon_remain_count, coupon_share_url='$coupon_share_url', coupon_start_fee='$coupon_start_fee', coupon_start_time='$coupon_start_time', coupon_total_count=$coupon_total_count, item_description='$item_description', item_id=$item_id, level_one_category_id=$level_one_category_id, level_one_category_name='$level_one_category_name', nick='$nick', pict_url='$pict_url', seller_id=$seller_id, shop_title=$shop_title, small_images=$small_images, title='$title', user_type=$user_type, volume=$volume, zk_final_price='$zk_final_price')"
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "OnSellContent(code=$code, `data`=$`data`, message='$message', success=$success)"
    }
}


