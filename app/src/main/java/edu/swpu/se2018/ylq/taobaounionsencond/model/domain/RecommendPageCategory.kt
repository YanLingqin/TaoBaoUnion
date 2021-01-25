package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class RecommendPageCategory(
    val code: Int,
    val `data`: List<RecommendPageCategoryData>,
    val message: String,
    val success: Boolean
)

data class RecommendPageCategoryData(
    val favorites_id: Int,
    val favorites_title: String,
    val type: Int
)