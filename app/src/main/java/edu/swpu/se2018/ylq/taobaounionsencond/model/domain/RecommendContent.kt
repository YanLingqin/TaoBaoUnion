package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

import android.text.TextUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class RecommendContent(
    val code: Int,
    val `data`: RecommendContentData,
    val message: String,
    val success: Boolean
)

data class RecommendContentData(
    val tbk_dg_optimus_material_response: TbkDgOptimusMaterialResponse
)

data class TbkDgOptimusMaterialResponse(
    val is_default: String,
    val request_id: String,
    val result_list: ResultList,
    val total_count: Int
)

data class ResultList(
    val map_data: List<MapData>
)

data class MapData(
    val category_id: Int,
    val click_url: String,
    val commission_rate: String,
    val coupon_amount: Int,
    val coupon_click_url: String,
    val coupon_end_time: String,
    val coupon_info: String,
    val coupon_remain_count: Int,
    val coupon_share_url: String,
    val coupon_start_fee: String,
    val coupon_start_time: String,
    val coupon_total_count: Int,
    val item_id: Long,
    val level_one_category_id: Int,
    val nick: String,
    val pict_url: String,
    val reserve_price: String,
    val seller_id: Long,
    val shop_title: String,
    val small_images: RecommendContentSmallImages,
    val title: String,
    val user_type: Int,
    val volume: Int,
    val white_image: String,
    val zk_final_price: String
) : IBaseInfo {
    override fun getCover(): String {
        return pict_url
    }

    override fun getGoodsTitle(): String {
        return title
    }

    override fun getGoodsUrl(): String {
        return if (TextUtils.isEmpty(coupon_click_url)) {
            coupon_click_url
        } else {
            click_url
        }
    }
}

data class RecommendContentSmallImages(
    val string: List<String>
)