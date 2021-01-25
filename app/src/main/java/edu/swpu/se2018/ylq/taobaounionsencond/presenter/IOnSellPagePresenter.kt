package edu.swpu.se2018.ylq.taobaounionsencond.presenter

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBasePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.view.IOnSellCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IOnSellPagePresenter :IBasePresenter<IOnSellCallback> {

    //加载特惠内容
    fun getOnSellContent()

    //重新加载，用于网络出错时
    fun reLoad()

    //加载更多
    fun loadMore()
}