package edu.swpu.se2018.ylq.taobaounionsencond.utils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class Constants {
    companion object{
        //base url
        const val BASE_URL = "https://api.sunofbeach.net/shop/"
        //home pager fragment bundle key
        const val KEY_HOME_PAGER_TITLE ="key_home_pager_title"
        const val KEY_HOME_PAGER_MATERIAL_ID  ="key_home_pager_material_id"

        //结果返回成功的
        const val SUCCESS_CODE = 10000
        //特惠默认页
        const val DEFAULT_ON_SELL_PAGE = 0
        //搜索默认页
        const val DEFAULT_SEARCH_PAGE = 0

        const val KEY_HISTORIES = "key_histories"

        //默认历史记录个数
        const val DEFAULT_HISTORIES_SIZE = 10

        //自定义组合控件默认大小
        const val DEFAULT_SPACE = 10F
    }
}