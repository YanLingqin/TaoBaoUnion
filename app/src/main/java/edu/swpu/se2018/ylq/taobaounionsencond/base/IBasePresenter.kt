package edu.swpu.se2018.ylq.taobaounionsencond.base

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IBasePresenter<T> {

    //注册UI通知的接口
    fun registerViewCallback(callback:T)

    //取消UI通知的接口
    fun unRegisterViewCallback(callback:T)
}