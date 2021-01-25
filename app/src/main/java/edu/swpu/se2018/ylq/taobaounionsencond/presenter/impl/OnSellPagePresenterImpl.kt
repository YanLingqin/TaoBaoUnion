package edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl

import edu.swpu.se2018.ylq.taobaounionsencond.model.Api
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.OnSellContent
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.IOnSellPagePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.Constants
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.RetrofitManager
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils
import edu.swpu.se2018.ylq.taobaounionsencond.view.IOnSellCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class OnSellPagePresenterImpl : IOnSellPagePresenter {
    private var mViewCallback: IOnSellCallback? = null

    private var mCurrentPage = Constants.DEFAULT_ON_SELL_PAGE
    private val retrofit = RetrofitManager.getRetrofit()
    private val api = retrofit.create(Api::class.java)
    override fun getOnSellContent() {
        if(mIsLoading){
            return
        }
        mIsLoading = true
        //通知UI状态为加载中、、、
        if (mViewCallback != null) {
            mViewCallback!!.onLoading()
        }
        //获取特惠内容
        val onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage)
        val task = api.getOnSellPageContent(onSellPageUrl)
        task.enqueue(object : Callback<OnSellContent> {
            override fun onResponse(call: Call<OnSellContent>, response: Response<OnSellContent>) {
                mIsLoading = false
                val code = response.code()
                LogUtils.d(this@OnSellPagePresenterImpl, "getOnSellContent  code --> $code")
                if (code == HttpURLConnection.HTTP_OK) {
                    val onSellContent = response.body()
                    onSuccess(onSellContent)
                } else {
                    onError()
                }
            }

            override fun onFailure(call: Call<OnSellContent>, t: Throwable) {
                onError()
            }

        })
    }

    private fun onSuccess(onSellContent: OnSellContent?) {
        if (mViewCallback != null) {
            if (onSellContent != null) {
                LogUtils.d(this@OnSellPagePresenterImpl, "发送数据成功")
                val resultList =
                    onSellContent.data.tbk_dg_optimus_material_response.result_list.map_data
                if (resultList.isEmpty()) {
                    mViewCallback!!.onEmpty()
                } else {
                    mViewCallback!!.onContentLoadedSuccess(onSellContent)
                }
            }
        }
    }

    private fun onError() {
        mIsLoading = false
        if (mViewCallback != null) {
            mViewCallback!!.onError()
        }
    }

    override fun reLoad() {
        //重新加载
        this.getOnSellContent()
    }

    private var mIsLoading = false

    override fun loadMore() {
        if(mIsLoading){
            return
        }
        mIsLoading = true
        //加载更多
        mCurrentPage++
        val onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage)
        val task = api.getOnSellPageContent(onSellPageUrl)
        task.enqueue(object : Callback<OnSellContent> {
            override fun onResponse(call: Call<OnSellContent>, response: Response<OnSellContent>) {
                mIsLoading = false
                val code = response.code()
                LogUtils.d(this@OnSellPagePresenterImpl, "getOnSellContent  code --> $code")
                if (code == HttpURLConnection.HTTP_OK) {
                    LogUtils.d(this@OnSellPagePresenterImpl,"mCurrentPage ---> $mCurrentPage")
                    val onSellContent = response.body()
                    onMoreLoaded(onSellContent)
                } else {
                    onMoreLoadedError()
                }
            }

            override fun onFailure(call: Call<OnSellContent>, t: Throwable) {
                onMoreLoadedError()
            }

        })
    }

    private fun onMoreLoadedError() {
        mIsLoading = false
        mCurrentPage--
        if (mViewCallback != null) {
            mViewCallback!!.onMoreLoadedError()
        }
    }

    private fun onMoreLoaded(onSellContent: OnSellContent?) {
        if (mViewCallback != null) {
            try {
                val mapData =
                    onSellContent!!.data.tbk_dg_optimus_material_response.result_list.map_data
                if (mapData.isEmpty()) {
                    mCurrentPage--
                    mViewCallback!!.onMoreLoadedEmpty()
                }else{
                    mViewCallback!!.onMoreLoaded(onSellContent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mViewCallback!!.onMoreLoadedEmpty()
            }
        }
    }

    override fun registerViewCallback(callback: IOnSellCallback) {
        this.mViewCallback = callback
    }

    override fun unRegisterViewCallback(callback: IOnSellCallback) {
        this.mViewCallback = null
    }
}