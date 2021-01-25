package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IBaseInfo {
    //获取商品封面
    fun getCover():String?
    //商品标题
    fun getGoodsTitle():String
    //商品的链接
    fun getGoodsUrl():String
}