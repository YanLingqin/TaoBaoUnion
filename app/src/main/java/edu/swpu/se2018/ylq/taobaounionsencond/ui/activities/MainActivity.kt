package edu.swpu.se2018.ylq.taobaounionsencond.ui.activities

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseActivity
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseFragment
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.ActivityMainBinding
import edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments.HomeFragment
import edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments.RecommendFragment
import edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments.OnSellFragment
import edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments.SearchFragment
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils

class MainActivity : BaseActivity() ,IMainActivity {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initFragments()
        initListener()
    }



    //设置响应事件
    private fun initListener() {
        if(this::mainBinding.isInitialized){
            mainBinding.mainNavigationBar.setOnNavigationItemSelectedListener { item ->
                //  根据不同的id来对不同的底部导航栏点击事件进行响应
                when (item.itemId) {
                    R.id.home -> {
                        LogUtils.d(this, "initListener: 切换到首页")
                        switchFragment(mHomeFragment)
                    }
                    R.id.recommend -> {
                        LogUtils.d(this, "initListener: 切换到精选")
                        switchFragment(mRecommendFragment)
                    }
                    R.id.red_packet -> {
                        LogUtils.d(this, "initListener: 切换到特惠")
                        switchFragment(mRedPacketFragment)
                    }
                    R.id.search -> {
                        LogUtils.d(this, "initListener: 切换到搜索")
                        switchFragment(mSearchFragment)
                    }
                }
                //返回true：表示我们消费了这个事件
                true
            }
        }
    }

    /**
     * 初始化各个fragment
     */
    lateinit var mHomeFragment: HomeFragment
    lateinit var mRecommendFragment: RecommendFragment
    lateinit var mRedPacketFragment: OnSellFragment
    lateinit var mSearchFragment: SearchFragment
    lateinit var mFm: FragmentManager

    private fun initFragments() {
        mHomeFragment = HomeFragment()
        mRedPacketFragment = OnSellFragment()
        mSearchFragment = SearchFragment()
        mRecommendFragment = RecommendFragment()
        mFm = supportFragmentManager
        switchFragment(mHomeFragment)
    }

    //上一次显示的fragment
    lateinit var lastOneFragment: BaseFragment

    //使用replace会销毁Fragment
    //需要使用add和Hide的方式来控制Fragment的切换
    private fun switchFragment(target: BaseFragment) {
        val beginTransaction = mFm.beginTransaction()
        if (!target.isAdded) {
            beginTransaction.add(R.id.main_page_container, target)
        } else {
            beginTransaction.show(target)
        }
        if (this::lastOneFragment.isInitialized && lastOneFragment!== target) {
            beginTransaction.hide(lastOneFragment)
        }
        lastOneFragment = target
        //beginTransaction.replace(R.id.main_page_container, target)
        beginTransaction.commit()
    }

    override fun switch2Search() {
        //switchFragment(mSearchFragment)
        //同时切换底部导航栏
        //TODO:
        mainBinding.mainNavigationBar.selectedItemId = R.id.search
    }
}