package edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.vondear.rxfeature.activity.ActivityScanerCode
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseFragment
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.BaseHomeFragmentLayoutBinding
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentHomeBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Categories
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.IHomePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.ui.activities.IMainActivity
import edu.swpu.se2018.ylq.taobaounionsencond.ui.activities.ScanQrCodeActivity
import edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter.HomePagerAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.PresenterManager
import edu.swpu.se2018.ylq.taobaounionsencond.view.IHomeCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class HomeFragment : BaseFragment(), IHomeCallback {
    override fun getSuccessViewResId() = R.layout.fragment_home

    private lateinit var mHomePresenter: IHomePresenter
    private var homeBinding: FragmentHomeBinding? = null
    private var mHomePagerAdapter: HomePagerAdapter? = null
    private var baseHomeFragmentLayoutBinding:BaseHomeFragmentLayoutBinding?=null

    override fun loadRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = inflater.inflate(R.layout.base_home_fragment_layout, container, false)
        baseHomeFragmentLayoutBinding = BaseHomeFragmentLayoutBinding.bind(view)
        return view
    }

    private lateinit var etHomeSearchInput: EditText
    private lateinit var ivScanIcon: ImageView

    override fun initView(rootView: View) {
        val frameLayout = rootView as FrameLayout
        homeBinding = FragmentHomeBinding.bind(frameLayout.getChildAt(0))
        //将tablayout与viewPager相结合
        homeBinding!!.homeIndicator.setupWithViewPager(homeBinding!!.homePager)
        //为viewPager设置适配器
        mHomePagerAdapter = HomePagerAdapter(childFragmentManager)
        //一定要设置嘛
        homeBinding!!.homePager.adapter = mHomePagerAdapter

        etHomeSearchInput = baseHomeFragmentLayoutBinding!!.etHomeSearchInput

        ivScanIcon = baseHomeFragmentLayoutBinding!!.ivScanIcon
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        LogUtils.d(this, "on Create View")
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d(this, " View  onDestroy")
    }

    override fun initPresenter() {
        //创建presenter
        mHomePresenter = PresenterManager.getHomePresenter()
        mHomePresenter.registerViewCallback(this)
    }

    override fun initListener() {
        super.initListener()
        etHomeSearchInput.setOnClickListener {
            //跳转到搜索页面
            val mainActivity = activity as IMainActivity
            mainActivity.switch2Search()
        }
        //扫码二维码点击按钮
        ivScanIcon.setOnClickListener {
            startActivity(Intent(context,ScanQrCodeActivity::class.java))
        }
    }

    override fun loadData() {
        //加载数据
        mHomePresenter.getCategories()
    }

    override fun onCategoriesLoaded(categories: Categories) {
        setUpState(State.SUCCESS)
        LogUtils.d(this, "onCategoriesLoaded....")
        //从这里获得加载回来的数据
        if (mHomePagerAdapter != null) {
            //通过这个属性，改变viewPAger的预加载
            //homeBinding.homePager.offscreenPageLimit = categories.data.size
            mHomePagerAdapter!!.setCategories(categories)
        }

    }

    override fun onError() {
        setUpState(State.ERROR)
    }

    override fun onLoading() {
        setUpState(State.LOADING)
    }

    override fun onEmpty() {
        setUpState(State.EMPTY)
    }

    //释放资源
    override fun release() {
        //取消该页面的回调注册
        mHomePresenter.unRegisterViewCallback(this)
        if (homeBinding != null) {
            homeBinding = null
        }
        if (baseHomeFragmentLayoutBinding != null) {
            baseHomeFragmentLayoutBinding = null
        }
        if (homeBinding != null) {
            homeBinding = null
        }
    }

    override fun onRetryClick() {
        //网络错误重试，重新加载分类
        mHomePresenter.getCategories()
    }

}