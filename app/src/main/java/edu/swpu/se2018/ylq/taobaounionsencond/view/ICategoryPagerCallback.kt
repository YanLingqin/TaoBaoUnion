package edu.swpu.se2018.ylq.taobaounionsencond.view

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBaseViewCallback
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.HomePagerContentDetail

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ICategoryPagerCallback :IBaseViewCallback{

    //数据加载完成
    fun onContentLoaded(contents: List<HomePagerContentDetail>)

    fun getCategoryId():Int

    //加载更多出现错误
    fun onLoaderMoreError()

    //没有更多内容
    fun onLoaderMoreEmpty()

    //加载更多内容成功
    fun onLoaderMoreLoaded(contents: List<HomePagerContentDetail>)

    //轮播图加载
    fun onLooperListLoaded(contents: List<HomePagerContentDetail>)


}