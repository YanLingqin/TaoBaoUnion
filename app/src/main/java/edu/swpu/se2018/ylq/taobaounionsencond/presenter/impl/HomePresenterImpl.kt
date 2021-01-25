package edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl

import edu.swpu.se2018.ylq.taobaounionsencond.model.Api
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Categories
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.IHomePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.RetrofitManager
import edu.swpu.se2018.ylq.taobaounionsencond.view.IHomeCallback
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
class HomePresenterImpl : IHomePresenter {
    private var mCallback: IHomeCallback?=null

    override fun getCategories() {
        if (mCallback!=null){
            mCallback!!.onLoading()
        }
        //加载分类数据
        val retrofit = RetrofitManager.getRetrofit()
        val task = retrofit.create(Api::class.java).getCategories()
        task.enqueue(object :Callback<Categories>{
            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                //成功
                val code = response.code()
                LogUtils.d(this@HomePresenterImpl,"result code is $code")
                if(code==HttpURLConnection.HTTP_OK){
                    //请求成功
                    var categories = response.body()
                    //LogUtils.d(this@HomePresenterImpl,categories.toString())
                    //为页面中的onCategoriesLoaded 方法设置数据，这样页面就可以得到数据了
                    if(mCallback!=null){
                        if (categories == null ||categories.data.isEmpty()) {
                            mCallback!!.onEmpty()
                        }else{
                            mCallback!!.onCategoriesLoaded(categories)
                        }
                    }
                }else{
                    //请求失败
                    LogUtils.i(this@HomePresenterImpl,"请求失败")
                    if(mCallback!=null){
                        mCallback!!.onError()
                    }
                }
            }

            override fun onFailure(call: Call<Categories>, t: Throwable) {
                //失败
                LogUtils.e(this@HomePresenterImpl,"请求错误 --->${t}")
                if(mCallback!=null){
                    mCallback!!.onError()
                }
            }

        })
    }

    override fun registerViewCallback(callback: IHomeCallback) {
        this.mCallback= callback
    }

    override fun unRegisterViewCallback(callback: IHomeCallback) {
        mCallback = null
    }
}