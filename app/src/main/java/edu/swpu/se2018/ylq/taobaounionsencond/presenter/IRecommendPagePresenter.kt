package edu.swpu.se2018.ylq.taobaounionsencond.presenter

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBasePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendContentData
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendPageCategoryData
import edu.swpu.se2018.ylq.taobaounionsencond.view.IRecommendPageCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IRecommendPagePresenter : IBasePresenter<IRecommendPageCallback> {

    //获取分类
    fun getCategories()

    //根据分类获取内容
    fun getContentByCategory(item : RecommendPageCategoryData)

    //重新加载内容
    fun reloadContent()
}