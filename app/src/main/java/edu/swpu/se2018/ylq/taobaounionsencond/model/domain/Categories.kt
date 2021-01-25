package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class Categories(
    val code: Int,
    val `data`: List<CategoryData>,
    val message: String,
    val success: Boolean
){
    override fun toString(): String {
        return "Categories(code=$code, `data`=$`data`, message='$message', success=$success)"
    }
}

data class CategoryData(
    val id: Int,
    val title: String
){
    override fun toString(): String {
        return "CategoryData(id=$id, title='$title')"
    }
}