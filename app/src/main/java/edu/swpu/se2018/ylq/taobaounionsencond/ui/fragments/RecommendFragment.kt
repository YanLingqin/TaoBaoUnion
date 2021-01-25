package edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments


import android.content.Intent
import android.graphics.Rect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseFragment
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentRecommendBinding
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentWithBarLayoutBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.*
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.IRecommendPagePresenter
import edu.swpu.se2018.ylq.taobaounionsencond.ui.activities.TicketActivity
import edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter.RecommendPageContentAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter.RecommendPageLeftAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.PresenterManager
import edu.swpu.se2018.ylq.taobaounionsencond.utils.SizeUtils
import edu.swpu.se2018.ylq.taobaounionsencond.utils.TicketUtil
import edu.swpu.se2018.ylq.taobaounionsencond.view.IRecommendPageCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendFragment : BaseFragment(), IRecommendPageCallback,
    RecommendPageLeftAdapter.OnLeftItemClickListener,
    RecommendPageContentAdapter.OnRecommendPageContentItemClick {

    override fun getSuccessViewResId() = R.layout.fragment_recommend

    private var fragmentRecommendBinding: FragmentRecommendBinding? = null
    private lateinit var rvLeftCategory: RecyclerView
    private lateinit var rvRightContent: RecyclerView
    private lateinit var recommendPageLeftAdapter: RecommendPageLeftAdapter
    private lateinit var recommendPageRightAdapter: RecommendPageContentAdapter

    private lateinit var tvFragmentBarTitle: TextView
    private var fragmentWithBarLayoutBinding: FragmentWithBarLayoutBinding? = null
    override fun loadRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = inflater.inflate(R.layout.fragment_with_bar_layout, container, false)
        fragmentWithBarLayoutBinding =
            FragmentWithBarLayoutBinding.bind(view)
        return view
    }

    override fun initView(rootView: View) {
        setUpState(State.SUCCESS)
        tvFragmentBarTitle = fragmentWithBarLayoutBinding!!.tvFragmentBarTitle
        tvFragmentBarTitle.text = rootView.resources.getText(R.string.txt_recommend_product)
        LogUtils.d(this,"title is ----> ${tvFragmentBarTitle.text}")
        fragmentRecommendBinding = FragmentRecommendBinding.bind(rootView)
        rvLeftCategory = fragmentRecommendBinding!!.rvLeftCategory
        rvLeftCategory.layoutManager = LinearLayoutManager(context)
        recommendPageLeftAdapter = RecommendPageLeftAdapter()
        rvLeftCategory.adapter = recommendPageLeftAdapter

        rvRightContent = fragmentRecommendBinding!!.rvRightContent
        rvRightContent.layoutManager = LinearLayoutManager(context)
        recommendPageRightAdapter = RecommendPageContentAdapter()
        rvRightContent.adapter = recommendPageRightAdapter
        rvRightContent.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = SizeUtils.dip2px(context!!, 4f)
                outRect.bottom = SizeUtils.dip2px(context!!, 4f)
                outRect.left = SizeUtils.dip2px(context!!, 6f)
                outRect.right = SizeUtils.dip2px(context!!, 6f)
            }
        })
    }

    private lateinit var recommendPagePresenter: IRecommendPagePresenter
    override fun initPresenter() {
        super.initPresenter()
        recommendPagePresenter = PresenterManager.getRecommendPagePresenter()
        recommendPagePresenter.registerViewCallback(this)
        recommendPagePresenter.getCategories()
    }

    override fun initListener() {
        super.initListener()
        recommendPageLeftAdapter.setOnLeftItemClickListener(this)

        recommendPageRightAdapter.setOnRecommendPageContentItemClick(this)

    }

    override fun release() {
        super.release()
        if (this::recommendPagePresenter.isInitialized) {
            recommendPagePresenter.unRegisterViewCallback(this)
        }
        if (fragmentRecommendBinding != null) {
            fragmentRecommendBinding = null
        }
    }

    override fun onRetryClick() {
        //重试
        recommendPagePresenter.reloadContent()
    }

    override fun onCategoriesLoaded(categories: RecommendPageCategory) {
        setUpState(State.SUCCESS)
        //分类的内容
        LogUtils.d(this, "categories ---> $categories")
        //更新UI
        recommendPageLeftAdapter.setData(categories)
    }

    override fun onContentLoaded(content: RecommendContent) {
        rvRightContent.scrollToPosition(0)
        //某个分类的内容
        LogUtils.d(this, "content ---> ${content}")
        recommendPageRightAdapter.setData(content)
    }

    override fun onError() {
        setUpState(State.ERROR)
    }

    override fun onLoading() {
        setUpState(State.LOADING)
    }

    override fun onEmpty() {

    }

    override fun itemClick(item: RecommendPageCategoryData) {
        LogUtils.d(
            this@RecommendFragment,
            "current recommend item  ---> ${item.favorites_title}"
        )
        //根据点击的不同分类，来获取分类内容
        recommendPagePresenter.getContentByCategory(item)
    }

    override fun onContentItemClick(item: MapData) {
        TicketUtil.toTicketPage(context!!,item)
    }

}