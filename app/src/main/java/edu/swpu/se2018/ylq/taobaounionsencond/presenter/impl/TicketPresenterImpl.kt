package edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl

import edu.swpu.se2018.ylq.taobaounionsencond.model.Api
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.TicketParams
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.TicketResult
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.ITicketPresenter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.RetrofitManager
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils
import edu.swpu.se2018.ylq.taobaounionsencond.view.ITicketPagerCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TicketPresenterImpl : ITicketPresenter {

    private var mCover: String? = null
    private var mViewCallback: ITicketPagerCallback? = null

    enum class LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private var ticketResult: TicketResult? = null
    private var currentState = LoadState.NONE

    override fun getTicket(title: String, url: String, cover: String?) {
        currentState = LoadState.LOADING
        this.onTicketLoadedLoading()
        LogUtils.d(this, "url ----> $url")
        this.mCover = cover
        val retrofit = RetrofitManager.getRetrofit()
        val api = retrofit.create(Api::class.java)

        val ticketUrl = UrlUtils.getTicketUrl(url)
        val ticketParams = TicketParams(ticketUrl, title)
        val task = api.getTicket(ticketParams)
        task.enqueue(object : Callback<TicketResult> {
            override fun onResponse(call: Call<TicketResult>, response: Response<TicketResult>) {
                val code = response.code()
                LogUtils.d(this@TicketPresenterImpl, "response code --->$code")
                if (code == HttpURLConnection.HTTP_OK) {
                    //成功
                    ticketResult = response.body()
                    LogUtils.d(this@TicketPresenterImpl, "result --->$ticketResult")
                    //通知UI
                    onTicketLoadedSuccess()
                } else {
                    onLoadedTicketError()

                }
            }

            override fun onFailure(call: Call<TicketResult>, t: Throwable) {
                onLoadedTicketError()
            }

        })

    }

    private fun onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback!!.onError()
        } else {
            currentState = LoadState.ERROR
        }

    }

    override fun registerViewCallback(callback: ITicketPagerCallback) {
        this.mViewCallback = callback
        if (currentState != LoadState.NONE) {
            //状态进行了变化
            //更新UI
            when (currentState) {
                LoadState.SUCCESS -> {
                    onTicketLoadedSuccess()
                }
                LoadState.ERROR -> {
                    onLoadedTicketError()
                }
                LoadState.LOADING -> {
                    onTicketLoadedLoading()
                }
            }
        }
    }

    override fun unRegisterViewCallback(callback: ITicketPagerCallback) {
        this.mViewCallback = null
    }

    private fun onTicketLoadedLoading() {
        mViewCallback?.onLoading()
    }

    private fun onTicketLoadedSuccess() {
        if (mViewCallback != null) {
            mViewCallback!!.onTicketLoaded(mCover, ticketResult)
        } else {
            currentState = LoadState.SUCCESS
        }
    }

}