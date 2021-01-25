package edu.swpu.se2018.ylq.taobaounionsencond.presenter

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBasePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.view.ISearchViewCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ISearchPresenter :IBasePresenter<ISearchViewCallback>  {
    //获取历史记录
    fun getHistories()
    //删除历史
    fun delHistories()

    //发起搜索
    fun doSearch(keyword:String)
    //重新搜索
    fun research()
    //加载更多结果
    fun loadMore()
    //获取推荐词
    fun getRecommendWords()
}