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
interface IBaseViewCallback {

    //网络出错
    fun onError()

    fun onLoading()

    fun onEmpty()

}