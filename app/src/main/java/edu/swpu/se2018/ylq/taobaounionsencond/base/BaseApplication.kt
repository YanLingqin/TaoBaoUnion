package edu.swpu.se2018.ylq.taobaounionsencond.base

import android.app.Application
import android.content.Context

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class BaseApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        appContext = baseContext
    }

    companion object {
        private lateinit var appContext: Context
        fun getAppContext(): Context {
            return appContext
        }
    }

}