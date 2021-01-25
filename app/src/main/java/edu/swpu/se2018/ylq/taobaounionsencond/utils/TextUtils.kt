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
class UrlUtils {

    companion object {
        fun createHomePager(materialId: Int, page: Int): String {
            return "discovery/$materialId/$page"
        }

        fun getCoverPath(pictUrl: String): String {
            return if (pictUrl.startsWith("http") || pictUrl.startsWith("https")) {
                pictUrl
            } else {
                "https:$pictUrl"
            }
        }

        fun getCoverPath(pictUrl: String?, size: Int): String? {
            if(pictUrl==null){
                return null
            }
            return if (pictUrl.startsWith("http") || pictUrl.startsWith("https")) {
                pictUrl + "_$size" + "x$size.jpg"
            } else {
                "https:$pictUrl" + "_$size" + "x$size.jpg"
            }
        }

        fun getTicketUrl(url: String): String {
            return if (url.startsWith("http") || url.startsWith("https")) {
                url
            } else {
                "https:$url"
            }
        }

        fun getRecommendPageContent(favoritesId: Int): String {
            return "recommend/$favoritesId"
        }

        fun getOnSellPageUrl(mCurrentPage: Int):String {
            return "onSell/$mCurrentPage"
        }

        fun getSearchUrl(page: Int, keyword: String) :String{
            return "search?page=${page}&keyword=${keyword}"
        }

    }

}