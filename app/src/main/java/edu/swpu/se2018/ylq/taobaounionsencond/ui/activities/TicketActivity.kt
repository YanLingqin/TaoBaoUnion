package edu.swpu.se2018.ylq.taobaounionsencond.ui.activities

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseActivity
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.ActivityTicketBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.TicketResult
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.ITicketPresenter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.PresenterManager
import edu.swpu.se2018.ylq.taobaounionsencond.utils.ToastUtil
import edu.swpu.se2018.ylq.taobaounionsencond.utils.UrlUtils
import edu.swpu.se2018.ylq.taobaounionsencond.view.ITicketPagerCallback
import kotlin.math.min

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class TicketActivity : BaseActivity(), ITicketPagerCallback {
    private var ticketBinding: ActivityTicketBinding? = null

    private var hasTaobaoApp = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ticketBinding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(ticketBinding!!.root)
        initListener()
        initPresenter()
    }

    private fun initListener() {
        ticketBinding?.ivTicketBack?.setOnClickListener {
            LogUtils.d(this, "ivTicketBack--->")
            finish()
        }

        ticketBinding?.tvCopyOrOpen!!.setOnClickListener {
            //复制淘口令
            val ticketCode = ticketBinding!!.etTicketCode.text.toString().trim()
            LogUtils.d(this, "ticketCode --> $ticketCode")
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            //复制到粘贴板
            val clipData = ClipData.newPlainText("ylq_tao_bao_ticket_code", ticketCode)
            cm.setPrimaryClip(clipData)

            //判断是否有淘宝
            if (hasTaobaoApp) {
                //如果有就打开淘宝
                val taobaoIntent = Intent()
                //taobaoIntent.action = "android.intent.action.MAIN"
                //taobaoIntent.addCategory("android.intent.category.LAUNCHER")
                val componentName =
                    ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity")
                taobaoIntent.component = componentName
                startActivity(taobaoIntent)
            } else {
                //没有就提示复制成功
                ToastUtil.showToast("已经复制，粘贴分享，或打开淘宝")
            }
        }
    }

    private var mTicketPresenter: ITicketPresenter? = null

    fun initPresenter() {
        mTicketPresenter = PresenterManager.getTicketPresenter()
        mTicketPresenter!!.registerViewCallback(this)
        //判断手机中是否安装有淘宝
        //act=android.intent.action.VIEW dat=http://m.taobao.com/...
        // flg=0x4000000 pkg=com.taobao.taobao
        // cmp=com.taobao.taobao/com.taobao.tao.TBMainActivity (has extras)} from uid 10255
        //act=android.intent.action.MAIN
        // cat=[android.intent.category.LAUNCHER]
        // flg=0x10200000
        // cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
        hasTaobaoApp = isApplicationInstall(this,"com.taobao.taobao")
        LogUtils.d(this, "hasTaobaoAPP ---> $hasTaobaoApp")
        //根据是否有淘宝App修改UI
        updateTipsText()
    }

    private fun isApplicationInstall(context: Context,packageName: String): Boolean {
        //坚持是否安装淘宝
        val packageManager = context.packageManager
        val installedPackages = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        installedPackages.forEach {
            if (it.packageName == packageName) {
                return true
            }
        }
        return false
    }

    private fun updateTipsText() {
        ticketBinding?.tvCopyOrOpen!!.text = if (hasTaobaoApp) {
            "打开淘宝领券"
        } else {
            "复制淘口令"
        }
    }

    override fun release() {
        mTicketPresenter?.unRegisterViewCallback(this)
    }

    override fun onTicketLoaded(cover: String?, result: TicketResult?) {
        //设置图片
        if (ticketBinding?.ivTicketCover != null && !TextUtils.isEmpty(cover)) {
            //LogUtils.d(this,"cover is ----> $cover")
            val layoutParams = ticketBinding?.ivTicketCover!!.layoutParams
            val width = layoutParams.width
            val height = layoutParams.height
            //LogUtils.d(this,"cover width ---> $width")
            //LogUtils.d(this,"cover height ---> $height")
            val coverSize = min(width, height) / 100 * 100
            LogUtils.d(this, "coverSize---> $coverSize")
            Glide.with(this).load(UrlUtils.getCoverPath(cover, coverSize))
                .into(ticketBinding?.ivTicketCover!!)
        }
        if(TextUtils.isEmpty(cover)){
            ticketBinding?.ivTicketCover!!.setImageResource(R.mipmap.empty)
        }
        //设置code
        if (ticketBinding?.etTicketCode != null && result != null && result.data.tbk_tpwd_create_response != null) {
            ticketBinding?.etTicketCode!!.setText(result.data.tbk_tpwd_create_response.data.model)
        }
        if (ticketBinding?.ticketCoverLoading != null) {
            ticketBinding?.ticketCoverLoading!!.visibility = View.GONE
        }
    }

    override fun onError() {
        if (ticketBinding?.ticketCoverLoading != null) {
            ticketBinding?.ticketCoverLoading!!.visibility = View.GONE
        }
        if (ticketBinding?.ticketLoadRetry != null) {
            ticketBinding?.ticketLoadRetry!!.visibility = View.VISIBLE
        }
    }

    override fun onLoading() {
        if (ticketBinding?.ticketCoverLoading != null) {
            ticketBinding?.ticketCoverLoading!!.visibility = View.VISIBLE
        }
        if (ticketBinding?.ticketLoadRetry != null) {
            ticketBinding?.ticketLoadRetry!!.visibility = View.GONE
        }
    }

    override fun onEmpty() {

    }


}