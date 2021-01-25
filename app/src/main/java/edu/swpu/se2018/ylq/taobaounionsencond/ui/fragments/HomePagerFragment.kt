package edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments


import android.graphics.Rect
import android.os.Bundle

import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseFragment
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentHomePagerBinding
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.IncludeHomePagerTitlePartBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.CategoryData
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.HomePagerContentDetail
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.ILinearItemInfo
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.ICategoryPagerPresenter
import edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter.LinearItemContentAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter.LooperPagerAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.*
import edu.swpu.se2018.ylq.taobaounionsencond.view.ICategoryPagerCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class HomePagerFragment : BaseFragment(), ICategoryPagerCallback,
    LinearItemContentAdapter.OnListenItemClickListener,
    LooperPagerAdapter.OnLooperPageItemClickListener {

    companion object {
        fun newInstance(category: CategoryData): HomePagerFragment {
            val homePagerFragment = HomePagerFragment()
            val bundle = Bundle()
            bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.title)
            bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.id)
            homePagerFragment.arguments = bundle
            return homePagerFragment
        }
    }

    override fun getSuccessViewResId(): Int {
        return R.layout.fragment_home_pager
    }

    override fun onResume() {
        super.onResume()
        //开始轮播
        homePagerBinding!!.looperPager.startLoop()
    }

    override fun onPause() {
        super.onPause()
        //停止轮播
        homePagerBinding!!.looperPager.stopLoop()
    }

    private var homePagerBinding: FragmentHomePagerBinding? = null
    private lateinit var homePagerContentAdapter: LinearItemContentAdapter
    private var includeHomePagerTitlePartBinding: IncludeHomePagerTitlePartBinding? = null

    override fun initView(rootView: View) {
        setUpState(State.SUCCESS)
        val linearLayout = rootView as FrameLayout
        homePagerBinding = FragmentHomePagerBinding.bind(linearLayout.getChildAt(0))
        includeHomePagerTitlePartBinding =
            IncludeHomePagerTitlePartBinding.bind(linearLayout.getChildAt(0))
        //设置布局管理器
        homePagerBinding!!.rvHomePageContent.layoutManager = LinearLayoutManager(context)
        homePagerBinding!!.rvHomePageContent.addItemDecoration(object :
            RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = SizeUtils.dip2px(context!!,2.5f)
                outRect.bottom = SizeUtils.dip2px(context!!,2.5f)
            }
        })
        //设置适配器
        homePagerContentAdapter = LinearItemContentAdapter()
        homePagerBinding!!.rvHomePageContent.adapter = homePagerContentAdapter

        //创建viewPager适配器（完成轮播图）
        looperPagerAdapter = LooperPagerAdapter()
        looperPagerAdapter.setOnLooperPageItemClickListener(this)
        homePagerBinding!!.looperPager.adapter = looperPagerAdapter

        //设置相关内容 TwinklingRefreshLayout
        homePagerBinding!!.twinklingRefreshLayout.setEnableRefresh(false)
        homePagerBinding!!.twinklingRefreshLayout.setEnableLoadmore(true)
    }


    override fun initListener() {
        homePagerContentAdapter.setOnListenItemClickListener(this)

        homePagerBinding!!.homePagerParent.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //获取头部整体的高度（一个looper和标题）
                val looperHeaderHeight =
                    homePagerBinding!!.homePagerLooperHeaderContainer.measuredHeight
                homePagerBinding!!.homePagerNestScroller.setLooperHeaderHeight(looperHeaderHeight)
                //LogUtils.d(this@HomePagerFragment,"looperHeaderHeight --->$looperHeaderHeight")

                val measuredHeight = homePagerBinding!!.homePagerParent.measuredHeight
                //LogUtils.d(this, " homePagerBinding.homePagerParent measuredHeight -->$measuredHeight")
                val layoutParams = homePagerBinding!!.rvHomePageContent.layoutParams
                layoutParams.height = measuredHeight
                homePagerBinding!!.rvHomePageContent.layoutParams = layoutParams
                if (measuredHeight != 0) {
                    homePagerBinding!!.homePagerParent.viewTreeObserver.removeOnGlobalLayoutListener(
                        this
                    )
                }
            }

        })

        //轮播图点击滑动监听
        homePagerBinding!!.looperPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (looperPagerAdapter.getDataSize() == 0) {
                    return
                }
                val targetPosition = position % looperPagerAdapter.getDataSize()
                //LogUtils.d(this@HomePagerFragment, "onPageSelected:position ---> $position")
                //LogUtils.d(
                //    this@HomePagerFragment,
                //    "onPageSelected:targetPosition ---> $targetPosition"
                //)
                //切换指示器
                updateLooperIndicator(targetPosition)
            }

            override fun onPageScrollStateChanged(state: Int) {
                //todo：可以尝试做下拉刷新
            }

        })

        //设置twinklingRefreshLayout的监听
        homePagerBinding!!.twinklingRefreshLayout.setOnRefreshListener(object :
            RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                LogUtils.d(this@HomePagerFragment, "load more")
                //  设置停止加载、、、
                //homePagerBinding.twinklingRefreshLayout.finishLoadmore()
                mCategoryPagerPresenter.loadMore(materialId)
            }

            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
            }
        })

        includeHomePagerTitlePartBinding!!.homePagerTitle.setOnClickListener {
            LogUtils.d(
                this,
                "RecyclerView is Height --> ${homePagerBinding!!.rvHomePageContent.measuredHeight}"
            )
        }


    }

    //更新指示器
    private fun updateLooperIndicator(targetPosition: Int) {
        for (i in 0 until homePagerBinding!!.looperPointContainer.childCount) {
            //LogUtils.d(this,"updateLooperIndicator:homePagerBinding.looperPointContainer.childCount  -->${homePagerBinding.looperPointContainer.childCount}")
            val point = homePagerBinding!!.looperPointContainer.getChildAt(i)
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected)
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal)
            }
        }
    }

    private lateinit var looperPagerAdapter: LooperPagerAdapter

    private lateinit var mCategoryPagerPresenter: ICategoryPagerPresenter
    override fun initPresenter() {
        mCategoryPagerPresenter = PresenterManager.getCategoryPagerPresenter()
        mCategoryPagerPresenter.registerViewCallback(this)
    }

    private var materialId: Int = 0
    override fun loadData() {
        val arguments = arguments
        val title = arguments!!.getString(Constants.KEY_HOME_PAGER_TITLE)
        materialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID)
        LogUtils.d(this, "loadData:title --> $title")
        LogUtils.d(this, "loadData:materialId --> $materialId")
        //加载数据
        mCategoryPagerPresenter.getContentByCategoryId(materialId)
        includeHomePagerTitlePartBinding!!.homePagerTitle.text = title
    }

    override fun onContentLoaded(contents: List<HomePagerContentDetail>) {
        //数据加载成功时调用
        //更新ui
        homePagerContentAdapter.setData(contents)
        setUpState(State.SUCCESS)
    }

    override fun getCategoryId(): Int {
        return materialId
    }

    override fun onLoading() {
        setUpState(State.LOADING)
    }

    override fun onError() {
        setUpState(State.ERROR)
    }

    override fun onEmpty() {
        setUpState(State.EMPTY)
    }

    override fun onLoaderMoreError() {
        ToastUtil.showToast("加载错误，请重试")
        homePagerBinding!!.twinklingRefreshLayout.finishLoadmore()
    }

    override fun onLoaderMoreEmpty() {
        ToastUtil.showToast("全部数据加载完成，已无更多")
        homePagerBinding!!.twinklingRefreshLayout.finishLoadmore()
    }

    override fun onLoaderMoreLoaded(contents: List<HomePagerContentDetail>) {
        LogUtils.d(this, "contents size -->${contents.size}")
        //添加到适配器数据底部
        homePagerContentAdapter.addData(contents)
        homePagerBinding!!.twinklingRefreshLayout.finishLoadmore()
        ToastUtil.showToast("加载了${contents.size}条数据")
    }

    override fun onLooperListLoaded(contents: List<HomePagerContentDetail>) {
        //轮播图数据加载
        looperPagerAdapter.setData(contents)

        //设置到中间点  不一定为0，就不在第一个
        val dx = (Int.MAX_VALUE / 2) % contents.size
        val targetCenterPosition = Int.MAX_VALUE / 2 - dx
        //处理一下使其保存在第一个图片
        homePagerBinding!!.looperPager.currentItem = targetCenterPosition
        //动态添加点
        for (i in contents.indices) {
            val point = View(context)
            val layoutParams = LinearLayout.LayoutParams(
                SizeUtils.dip2px(context!!, 8f),
                SizeUtils.dip2px(context!!, 8f)
            )
            layoutParams.leftMargin = SizeUtils.dip2px(context!!, 5f)
            layoutParams.rightMargin = SizeUtils.dip2px(context!!, 5f)
            point.layoutParams = layoutParams
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected)
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal)
            }
            homePagerBinding!!.looperPointContainer.addView(point)
        }
    }


    override fun release() {
        if (this::mCategoryPagerPresenter.isInitialized) {
            mCategoryPagerPresenter.unRegisterViewCallback(this)
        }
        if (homePagerBinding != null) {
            homePagerBinding = null
        }
        if (includeHomePagerTitlePartBinding != null) {
            includeHomePagerTitlePartBinding = null
        }
    }

    override fun onItemClick(item: ILinearItemInfo) {
        //列表内容
        LogUtils.d(this, "item click ==---> ${item.getGoodsTitle()}")
        handleItemClick(item as HomePagerContentDetail)
    }


    private fun handleItemClick(item: HomePagerContentDetail) {
        TicketUtil.toTicketPage(context!!,item)
//        //处理数据
//        val title = item.title
//        var url = item.coupon_click_url
//        if (TextUtils.isEmpty(url)) {
//            url = item.click_url
//        }
//        val cover = item.pict_url
//
//        //拿到ticketPresenter加载数据
//        val ticketPresenter = PresenterManager.getTicketPresenter()
//        ticketPresenter.getTicket(title, url, cover)
//        startActivity(Intent(context, TicketActivity::class.java))
    }

    override fun onLooperItemClick(item: HomePagerContentDetail) {
        //轮播图内容
        LogUtils.d(this, "onLooperItemClick click ==---> ${item.title}")
        handleItemClick(item)
    }

}