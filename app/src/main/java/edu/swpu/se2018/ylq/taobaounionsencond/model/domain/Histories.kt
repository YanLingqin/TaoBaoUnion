package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

import java.util.*
import kotlin.collections.ArrayList

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class Histories {

    private lateinit var histories: ArrayList<String>

    fun getHistories():ArrayList<String>{
        return histories
    }

    fun setHistories(histories:ArrayList<String>){
        this.histories = histories
    }

}