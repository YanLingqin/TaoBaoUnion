package edu.swpu.se2018.ylq.taobaounionsencond.presenter

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBasePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.view.ICategoryPagerCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ICategoryPagerPresenter :IBasePresenter<ICategoryPagerCallback>{

    //根据分类id去获取内容
    fun getContentByCategoryId(categoryId:Int )

    //加载更多
    fun loadMore(categoryId:Int)

    //重新加载
    fun reload(categoryId:Int )



}