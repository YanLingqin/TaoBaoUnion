package edu.swpu.se2018.ylq.taobaounionsencond.utils

import edu.swpu.se2018.ylq.taobaounionsencond.presenter.ICategoryPagerPresenter
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.IHomePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.ITicketPresenter
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl.*
import java.security.PrivateKey

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object PresenterManager {
    private val mCategoryPagerPresenterImpl = CategoryPagerPresenterImpl()
    private val mHomePresenterImpl = HomePresenterImpl()
    private val mTicketPresenterImpl = TicketPresenterImpl()
    private val mRecommendPagePresenterImpl = RecommendPagePresenterImpl()
    private val mOnSellPagePresenterImpl = OnSellPagePresenterImpl()
    private val mSearchPresenterImpl = SearchPresenterImpl()

    fun getCategoryPagerPresenter(): ICategoryPagerPresenter {
        return mCategoryPagerPresenterImpl
    }

    fun getHomePresenter():IHomePresenter{
        return mHomePresenterImpl
    }

    fun getTicketPresenter():ITicketPresenter{
        return mTicketPresenterImpl
    }

    fun getRecommendPagePresenter():RecommendPagePresenterImpl{
        return mRecommendPagePresenterImpl
    }

    fun getOnSellPagePresenter():OnSellPagePresenterImpl{
        return mOnSellPagePresenterImpl
    }

    fun getSearchPresenter():SearchPresenterImpl{
        return mSearchPresenterImpl
    }
}