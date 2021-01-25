package edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl

import edu.swpu.se2018.ylq.taobaounionsencond.model.Api
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendContent
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendContentData
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendPageCategory
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.RecommendPageCategoryData
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.IRecommendPagePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.RetrofitManager
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils
import edu.swpu.se2018.ylq.taobaounionsencond.view.IRecommendPageCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendPagePresenterImpl : IRecommendPagePresenter {

    private lateinit var mApi: Api

    init {
        val retrofit = RetrofitManager.getRetrofit()
        mApi = retrofit.create(Api::class.java)
    }

    private var mCallback: IRecommendPageCallback? = null

    override fun getCategories() {
        if (mCallback!=null){
            mCallback!!.onLoading()
        }
        val task = mApi.getRecommendPageCategories()
        task.enqueue(object : Callback<RecommendPageCategory> {
            override fun onResponse(
                call: Call<RecommendPageCategory>,
                response: Response<RecommendPageCategory>
            ) {
                val code = response.code()
                LogUtils.d(this@RecommendPagePresenterImpl, "code ---> $code")
                if (code == HttpURLConnection.HTTP_OK) {
                    //访问成功
                    val recommendPageCategory = response.body()
                    //LogUtils.d(
                    //    this@RecommendPagePresenterImpl,
                    //    "recommendPageCategory --->$recommendPageCategory"
                    //)
                    //通知UI更新
                    if (mCallback != null) {
                        if (recommendPageCategory == null || recommendPageCategory.data.isEmpty()) {
                            mCallback!!.onEmpty()
                        } else {
                            mCallback!!.onCategoriesLoaded(recommendPageCategory)
                        }
                    }
                } else {
                    //访问出错
                    LogUtils.d(this@RecommendPagePresenterImpl, "访问出错")
                    onLoadedError()
                }
            }

            override fun onFailure(call: Call<RecommendPageCategory>, t: Throwable) {
                LogUtils.d(this@RecommendPagePresenterImpl, "onFailure-->访问出错")
                onLoadedError()
            }

        })


    }

    private fun onLoadedError() {
        if (mCallback != null) {
            mCallback!!.onError()
        }
    }

    override fun getContentByCategory(item: RecommendPageCategoryData) {
        val recommendPageContentUrl = UrlUtils.getRecommendPageContent(item.favorites_id)
        LogUtils.d(this,"recommendPageContentUrl --->${recommendPageContentUrl}")
        val task =
            mApi.getRecommendPageContentByCategoryId(recommendPageContentUrl)
        task.enqueue(object : Callback<RecommendContent> {
            override fun onResponse(
                call: Call<RecommendContent>,
                response: Response<RecommendContent>
            ) {
                val code = response.code()
                LogUtils.d(this@RecommendPagePresenterImpl, "getContentByCategory :code ---> $code")
                if (code == HttpURLConnection.HTTP_OK) {
                    val recommendContent = response.body()
                    //LogUtils.d(
                    //    this@RecommendPagePresenterImpl,
                    //    "recommendContent --> $recommendContent"
                    //)
                    if (mCallback != null) {
                        if (recommendContent != null) {
                            mCallback!!.onContentLoaded(recommendContent)
                        }
                    }
                }else{
                    onLoadedError()
                }
            }

            override fun onFailure(call: Call<RecommendContent>, t: Throwable) {
                onLoadedError()
            }
        })
    }

    override fun reloadContent() {
       this.getCategories()
    }

    override fun registerViewCallback(callback: IRecommendPageCallback) {
        this.mCallback = callback
    }

    override fun unRegisterViewCallback(callback: IRecommendPageCallback) {
        this.mCallback = null
    }
}