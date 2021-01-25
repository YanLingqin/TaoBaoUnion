package edu.swpu.se2018.ylq.taobaounionsencond.utils

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.IBaseInfo
import edu.swpu.se2018.ylq.taobaounionsencond.ui.activities.TicketActivity

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TicketUtil {
    companion object{
        fun toTicketPage(context:Context,info:IBaseInfo){
            //处理数据
            val title = info.getGoodsTitle()
            var url = info.getGoodsUrl()
            if (TextUtils.isEmpty(url)) {
                url = info.getGoodsUrl()
            }
            val cover = info.getCover()
            //拿到ticketPresenter加载数据
            val ticketPresenter = PresenterManager.getTicketPresenter()
            ticketPresenter.getTicket(title, url, cover)
            context.startActivity(Intent(context, TicketActivity::class.java))
        }
    }

}