package edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl

import edu.swpu.se2018.ylq.taobaounionsencond.model.Api
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Histories
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.SearchRecommendResult
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.SearchResult
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.ISearchPresenter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.Constants
import edu.swpu.se2018.ylq.taobaounionsencond.utils.JsonCacheUtil
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.RetrofitManager
import edu.swpu.se2018.ylq.taobaounionsencond.view.ISearchViewCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SearchPresenterImpl : ISearchPresenter {
    private var mCurrentKeyword: String? = null
    private var mViewCallback: ISearchViewCallback? = null
    private val retrofit = RetrofitManager.getRetrofit()
    private val api = retrofit.create(Api::class.java)


    override fun getHistories() {
        val histories = JsonCacheUtil.getValue(Constants.KEY_HISTORIES, Histories::class.java)
        mViewCallback?.onHistoriesLoaded(histories)
    }

    override fun delHistories() {
        JsonCacheUtil.delCache(Constants.KEY_HISTORIES)
        mViewCallback?.onHistoriesDeleted()
    }

    private val historiesMaxSize = Constants.DEFAULT_HISTORIES_SIZE

    //添加历史记录
    private fun saveHistory(history: String) {
        var histories  =
            JsonCacheUtil.getValue(Constants.KEY_HISTORIES, Histories::class.java)
        //如果存在就先删除，在进行添加
        var historiesList: MutableList<String>? = null
        if (histories != null) {
            historiesList = histories.getHistories()
            if (historiesList.contains(history)) {
                historiesList.remove(history)
            }
        }
        //完成去重
        //处理数据用空的情况
        if (historiesList == null) {
            historiesList = ArrayList()
        }
        if (histories == null) {
            histories = Histories()
        }
        histories.setHistories(historiesList as ArrayList<String>)
        //对个数进行限制
        if (historiesList.size > historiesMaxSize) {
            historiesList = historiesList.subList(0, historiesMaxSize)
        }
        //添加记录
        historiesList.add(history)
        //保存记录
        JsonCacheUtil.saveCache(Constants.KEY_HISTORIES, histories)
    }

    private var mCurrentPage: Int = Constants.DEFAULT_SEARCH_PAGE

    override fun doSearch(keyword: String) {
        if (mCurrentKeyword != keyword) {
            this.saveHistory(keyword)
            this.mCurrentKeyword = keyword
        }
        //需要更新UI状态
        mViewCallback?.onLoading()
        //val searchUrl = UrlUtils.getSearchUrl(page, keyword)
        //val task = api.getSearchContent(searchUrl)
        val task = api.getSearchContent(mCurrentPage, keyword)
        task.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                val code = response.code()
                LogUtils.d(this@SearchPresenterImpl, "doSearch code -->$code")
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body())
                } else {
                    mViewCallback?.onError()
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                t.printStackTrace()
                mViewCallback?.onError()
            }

        })
    }

    private fun isResultEmpty(searchResult: SearchResult?): Boolean {
        try {
            return searchResult == null || searchResult.data.tbk_dg_material_optional_response.result_list.map_data.isEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun handleSearchResult(searchResult: SearchResult?) {
        mViewCallback?.let {
            if (isResultEmpty(searchResult)) {
                it.onEmpty()
            } else {
                it.onSearchSuccess(searchResult!!)
            }
        }
    }

    override fun research() {
        mCurrentKeyword?.let {
            this.doSearch(it)
        }
        if (mCurrentKeyword == null) {
            mViewCallback?.onEmpty()
        }

    }

    override fun loadMore() {
        mCurrentPage++
        //进行搜索
        if (mCurrentKeyword == null) {
            mViewCallback?.onEmpty()
        } else {
            doSearchMore()
        }

    }

    private fun doSearchMore() {
        val task = api.getSearchContent(mCurrentPage, mCurrentKeyword!!)
        task.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                val code = response.code()
                LogUtils.d(this@SearchPresenterImpl, "doSearchMore code -->$code")
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body())
                } else {
                    onLoaderMoreError()
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                t.printStackTrace()
                onLoaderMoreError()
            }
        })
    }

    //处理加载更多的结果
    private fun handleMoreSearchResult(searchResult: SearchResult?) {
        mViewCallback?.let {
            if (isResultEmpty(searchResult)) {
                it.onMoreLoaderEmpty()
            } else {
                it.onMoreLoaded(searchResult!!)
            }
        }
    }

    private fun onLoaderMoreError() {
        mCurrentPage--
        mViewCallback?.onMoreLoaderError()
    }

    override fun getRecommendWords() {
        val task = api.getSearchRecommend()
        task.enqueue(object : Callback<SearchRecommendResult> {
            override fun onResponse(
                call: Call<SearchRecommendResult>,
                response: Response<SearchRecommendResult>
            ) {
                val code = response.code()
                LogUtils.d(this@SearchPresenterImpl, "getRecommendWords code -->$code")
                if (code == HttpURLConnection.HTTP_OK) {
                    //处理结果
                    if (mViewCallback != null) {
                        val searchRecommendResult = response.body()
                        if (searchRecommendResult != null) {
                            mViewCallback!!.onRecommendWordsLoaded(searchRecommendResult.data)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SearchRecommendResult>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    override fun registerViewCallback(callback: ISearchViewCallback) {
        this.mViewCallback = callback
    }

    override fun unRegisterViewCallback(callback: ISearchViewCallback) {
        if (mViewCallback != null) {
            mViewCallback = null
        }
    }
}