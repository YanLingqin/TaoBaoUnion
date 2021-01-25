package edu.swpu.se2018.ylq.taobaounionsencond.view

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBaseViewCallback
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendContent
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendPageCategory

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IRecommendPageCallback :IBaseViewCallback {

    //分类内容结果
    fun onCategoriesLoaded(categories: RecommendPageCategory)

    fun onContentLoaded(content:RecommendContent)
}