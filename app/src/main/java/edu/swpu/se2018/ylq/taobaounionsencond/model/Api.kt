package edu.swpu.se2018.ylq.taobaounionsencond.model

import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.*
import retrofit2.Call
import retrofit2.http.*

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface Api {

    @GET("discovery/categories")
    fun getCategories(): Call<Categories>

    @GET
    fun getHomePageContent(
        @Url url: String
    ): Call<HomePagerContent>

    @POST("tpwd")
    fun getTicket(@Body ticketParams: TicketParams): Call<TicketResult>

    //获取精选内容分类
    @GET("recommend/categories")
    fun getRecommendPageCategories(): Call<RecommendPageCategory>

    //通过目录ID获取精选内容
    @GET
    fun getRecommendPageContentByCategoryId(@Url url: String): Call<RecommendContent>

    //获取特惠内容
    @GET
    fun getOnSellPageContent(@Url url: String): Call<OnSellContent>

    //获取搜索内容
    @GET
    fun getSearchContent(@Url url: String): Call<SearchResult>

    @GET("search")
    fun getSearchContent(
        @Query("page") page: Int,
        @Query("keyword") keyword: String
    ): Call<SearchResult>

    //获取搜索热词
    @GET("search/recommend")
    fun getSearchRecommend(): Call<SearchRecommendResult>
}