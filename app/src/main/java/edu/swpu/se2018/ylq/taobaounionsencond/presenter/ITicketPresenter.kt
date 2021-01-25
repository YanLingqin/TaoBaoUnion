package edu.swpu.se2018.ylq.taobaounionsencond.presenter

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBasePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.view.ITicketPagerCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ITicketPresenter :IBasePresenter<ITicketPagerCallback>{
    //获取优惠劵生成淘口令
    fun getTicket(title: String,url:String,cover:String?)

}