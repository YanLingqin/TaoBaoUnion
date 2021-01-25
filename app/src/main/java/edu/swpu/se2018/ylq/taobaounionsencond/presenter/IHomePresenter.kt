package edu.swpu.se2018.ylq.taobaounionsencond.presenter

import edu.swpu.se2018.ylq.taobaounionsencond.base.IBasePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.view.IHomeCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IHomePresenter :IBasePresenter<IHomeCallback>{
    //获取商品分类
    fun getCategories()
}