package edu.swpu.se2018.ylq.taobaounionsencond.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseApplication
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.CacheWithDuration
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Histories
import java.lang.reflect.Type

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object JsonCacheUtil {

    private const val JSON_CACHE_SP_NAME = "json_cache_sp_name"
    private val mSharedPreferences = BaseApplication.getAppContext()
        .getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE)
    private val mGson = Gson()
    fun saveCache(key: String, value: Any) {
        saveCache(key, value, -1L)
    }

    fun saveCache(key: String, value: Any, duration: Long) {
        mSharedPreferences.edit().apply {
            val valueStr = mGson.toJson(value)
            var duration = duration
            if (duration != -1L) {
                duration += System.currentTimeMillis()
            }
            //保存一个有数据有时间的内容
            val cacheWithDuration = CacheWithDuration(duration, valueStr)
            val cacheWithTime = mGson.toJson(cacheWithDuration)
            putString(key, cacheWithTime)
            apply()
        }

    }

    fun delCache(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }

    fun <T : Any?> getValue(key: String, clazz: Class<T>): T? {
        val valueWithDuration = mSharedPreferences.getString(key, null) ?: return null
        val cacheWithDuration = mGson.fromJson(valueWithDuration, CacheWithDuration::class.java)
        //对时间进行判断
        val duration = cacheWithDuration.duration
        if (duration != -1L && duration - System.currentTimeMillis() <= 0) {
            //已过期
            return null
        } else {
            //无限制时间
            val cache = cacheWithDuration.cache
            LogUtils.d(this,"cache ---> $cache")
            return mGson.fromJson(cache,clazz)
        }
    }

}