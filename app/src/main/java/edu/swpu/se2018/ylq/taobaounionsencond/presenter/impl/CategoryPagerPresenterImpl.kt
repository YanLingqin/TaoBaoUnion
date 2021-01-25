package edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl

import edu.swpu.se2018.ylq.taobaounionsencond.model.Api
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.HomePagerContent
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.HomePagerContentDetail
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.ICategoryPagerPresenter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.RetrofitManager
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils
import edu.swpu.se2018.ylq.taobaounionsencond.view.ICategoryPagerCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CategoryPagerPresenterImpl : ICategoryPagerPresenter {

    private val pagesInfo = mutableMapOf<Int, Int>()

    private  val DEFAULT_PAGE = 1
    override fun getContentByCategoryId(categoryId: Int) {
        callbacks.forEach {
            if (categoryId == it.getCategoryId()) {
                it.onLoading()
            }
        }
        //根据分类id，加载内容
        val targetPage = if (pagesInfo[categoryId] == null) {
            pagesInfo[categoryId] = DEFAULT_PAGE
            pagesInfo[categoryId]
        } else {
            pagesInfo[categoryId]
        }
        val task = createTask(categoryId, targetPage)
        task.enqueue(object : Callback<HomePagerContent> {
            override fun onResponse(
                call: Call<HomePagerContent>,
                response: Response<HomePagerContent>
            ) {
                val code = response.code()
                LogUtils.d(this, "code ---->$code")
                if (code == HttpURLConnection.HTTP_OK) {
                    val homePagerContent = response.body()
                    LogUtils.d(
                        this@CategoryPagerPresenterImpl,
                        "onResponse :homePagerContent--->$homePagerContent"
                    )
                    handleHomePageContentResult(homePagerContent, categoryId)
                } else {
                    LogUtils.e(this@CategoryPagerPresenterImpl, "onResponse:请求失败")
                    handleNetworkError(categoryId)
                }
            }

            override fun onFailure(call: Call<HomePagerContent>, t: Throwable) {
                LogUtils.d(this@CategoryPagerPresenterImpl, "onFailure:$t")
                handleNetworkError(categoryId)
            }

        })
    }

    private fun createTask(
        categoryId: Int,
        targetPage: Int?
    ): Call<HomePagerContent> {
        val api = RetrofitManager.getRetrofit().create(Api::class.java)
        val url = UrlUtils.createHomePager(categoryId, targetPage!!)
        LogUtils.d(this, "url --> $url")
        val task = api.getHomePageContent(url)
        return task
    }

    //处理网络错误
    private fun handleNetworkError(categoryId: Int) {
        callbacks.forEach {
            if (categoryId == it.getCategoryId()) {
                it.onError()
            }
        }
    }

    //处理返回结果
    private fun handleHomePageContentResult(homePagerContent: HomePagerContent?, categoryId: Int) {
        val data: List<HomePagerContentDetail> = homePagerContent!!.data
        for (i in callbacks) {
            if (categoryId == i.getCategoryId()) {
                if ( callbacks.isNotEmpty()) {
                    i.onContentLoaded(homePagerContent.data)
                    //通知轮播图的数据加载完成
                    val looperData = data.subList(data.size - 5, data.size)
                    LogUtils.d(this, "looperData size ---> ${looperData.size}")
                    i.onLooperListLoaded(looperData)
                } else {
                    i.onEmpty()
                }
            }
        }
    }

    private var currentPage: Int = DEFAULT_PAGE
    override fun loadMore(categoryId: Int) {
        //加载更多内容 1、获取页码 2、页码增加 3、加载数据 4、处理结果
        currentPage = pagesInfo[categoryId]!!
        if (pagesInfo[categoryId] != null) {
            currentPage++
            pagesInfo[categoryId] = currentPage
        }

        val task = createTask(categoryId, currentPage)
        task.enqueue(object : Callback<HomePagerContent> {
            override fun onResponse(
                call: Call<HomePagerContent>,
                response: Response<HomePagerContent>
            ) {
                val code = response.code()
                LogUtils.d(this@CategoryPagerPresenterImpl, "code --->$code")
                if (code == HttpURLConnection.HTTP_OK) {
                    val homePagerContent = response.body()
                    handleLoaderResult(homePagerContent, categoryId)
                } else {
                    handleLoaderMoreError(categoryId)
                }
            }

            override fun onFailure(call: Call<HomePagerContent>, t: Throwable) {
                //请求失败
                LogUtils.d(this@CategoryPagerPresenterImpl, t.toString())
                handleLoaderMoreError(categoryId)
            }

        })
    }

    //处理加载回来的结果
    private fun handleLoaderResult(homePagerContent: HomePagerContent?, categoryId: Int) {
        callbacks.forEach {
            if (it.getCategoryId() == categoryId) {
                if (homePagerContent == null || homePagerContent.data.isEmpty()) {
                    it.onLoaderMoreEmpty()
                } else {
                    it.onLoaderMoreLoaded(homePagerContent.data)
                }
            }
        }
    }

    //处理刷新加载错误
    private fun handleLoaderMoreError(categoryId: Int) {
        currentPage--
        pagesInfo[categoryId] = currentPage
        callbacks.forEach { callback ->
            if (callback.getCategoryId() == categoryId) {
                callback.onLoaderMoreError()
            }
        }
    }

    override fun reload(categoryId: Int) {
        callbacks.forEach { callback ->
            callback.onError()
        }
    }

    private val callbacks = ArrayList<ICategoryPagerCallback>()

    override fun registerViewCallback(callback: ICategoryPagerCallback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback)
        }
    }

    override fun unRegisterViewCallback(callback: ICategoryPagerCallback) {
        if (callbacks.contains(callback)) {
            callbacks.remove(callback)
        }
    }
}