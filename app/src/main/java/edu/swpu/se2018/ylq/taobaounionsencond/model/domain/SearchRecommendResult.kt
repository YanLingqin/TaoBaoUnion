package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class SearchRecommendResult(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val createTime: String,
        val id: String,
        val keyword: String
    )
}

