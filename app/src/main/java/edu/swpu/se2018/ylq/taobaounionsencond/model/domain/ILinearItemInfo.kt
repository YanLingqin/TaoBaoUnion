package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ILinearItemInfo :IBaseInfo {
    //获取原价
    fun getFinalPrice():String
    //获取优惠价格
    fun getCouponAmount() :Long
    //获取销量
    fun getSales():Long
}