package edu.swpu.se2018.ylq.taobaounionsencond.view

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBaseViewCallback
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.OnSellContent

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IOnSellCallback :IBaseViewCallback {

    //加载特惠内容成功
    fun onContentLoadedSuccess(result:OnSellContent)

    //加载更多成功
    fun onMoreLoaded(moreResult:OnSellContent)
    //加载更多失败
    fun onMoreLoadedError()
    //没有更多可加载
    fun onMoreLoadedEmpty()
}