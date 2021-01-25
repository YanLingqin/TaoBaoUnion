package edu.swpu.se2018.ylq.taobaounionsencond.view

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBaseViewCallback
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Histories
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.SearchRecommendResult
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.SearchResult

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ISearchViewCallback : IBaseViewCallback {
    //搜索历史记录
    fun onHistoriesLoaded(histories: Histories?)

    //历史记录被删除
    fun onHistoriesDeleted()

    //搜索成功结果
    fun onSearchSuccess(result: SearchResult)

    //成功加载更多内容
    fun onMoreLoaded(result: SearchResult)

    //加载更多出错
    fun onMoreLoaderError()

    //加载更多为空
    fun onMoreLoaderEmpty()

    //推荐词获取结果
    fun onRecommendWordsLoaded(recommendWords: List<SearchRecommendResult.Data>)
}