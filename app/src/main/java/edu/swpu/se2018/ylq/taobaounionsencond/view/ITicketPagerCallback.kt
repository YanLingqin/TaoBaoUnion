package edu.swpu.se2018.ylq.taobaounionsencond.view

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBaseViewCallback
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.TicketResult

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ITicketPagerCallback :IBaseViewCallback {
    fun onTicketLoaded(cover:String?,result: TicketResult?)
}