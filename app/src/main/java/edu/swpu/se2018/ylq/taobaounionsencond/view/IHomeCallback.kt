package edu.swpu.se2018.ylq.taobaounionsencond.view

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBaseViewCallback
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Categories

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IHomeCallback : IBaseViewCallback {
    //用于通知返回结果（请求是异步的）
    fun onCategoriesLoaded(categories: Categories)

}