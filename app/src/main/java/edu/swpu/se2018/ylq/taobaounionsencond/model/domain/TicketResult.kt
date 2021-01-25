package edu.swpu.se2018.ylq.taobaounionsencond.model.domain

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
data class TicketResult(
    val code: Int,
    val `data`: TicketData,
    val message: String,
    val success: Boolean

) {
    override fun toString(): String {
        return "TicketResult(code=$code, `data`=$`data`, message='$message', success=$success)"
    }
}

data class TicketData(
    val tbk_tpwd_create_response: TbkTpwdCreateResponse

) {
    override fun toString(): String {
        return "TicketData(tbk_tpwd_create_response=$tbk_tpwd_create_response)"
    }
}

data class TbkTpwdCreateResponse(
    val `data`: DataX,
    val request_id: String
) {
    override fun toString(): String {
        return "TbkTpwdCreateResponse(`data`=$`data`, request_id='$request_id')"
    }
}

data class DataX(
    val model: String
) {
    override fun toString(): String {
        return "DataX(model='$model')"
    }
}