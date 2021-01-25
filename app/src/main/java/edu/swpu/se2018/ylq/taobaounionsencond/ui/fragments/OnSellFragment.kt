package edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments


import android.content.Intent
import android.graphics.Rect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseFragment
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentOnSellBinding
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentWithBarLayoutBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.IBaseInfo
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.OnSellContent
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl.OnSellPagePresenterImpl
import edu.swpu.se2018.ylq.taobaounionsencond.ui.activities.TicketActivity
import edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter.OnSellPageContentAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.utils.*
import edu.swpu.se2018.ylq.taobaounionsencond.view.IOnSellCallback

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class OnSellFragment : BaseFragment(), IOnSellCallback,
    OnSellPageContentAdapter.OnOnSellContentItemClickListener {
    override fun getSuccessViewResId() = R.layout.fragment_on_sell

    private var fragmentOnSellBinding: FragmentOnSellBinding? = null
    private lateinit var rvOnSelContent: RecyclerView
    private lateinit var onSellRefreshLayout: TwinklingRefreshLayout
    private lateinit var onSellPageContentAdapter: OnSellPageContentAdapter

    private lateinit var tvFragmentBarTitle: TextView
    private var fragmentWithBarLayoutBinding: FragmentWithBarLayoutBinding? = null
    override fun loadRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = inflater.inflate(R.layout.fragment_with_bar_layout, container, false)
        fragmentWithBarLayoutBinding =
            FragmentWithBarLayoutBinding.bind(view)
        tvFragmentBarTitle = fragmentWithBarLayoutBinding!!.tvFragmentBarTitle
        tvFragmentBarTitle.text =view.resources.getText(R.string.txt_recommend_product)
        LogUtils.d(this,"title is ----> ${tvFragmentBarTitle.text}")
        return view
    }

    override fun initView(rootView: View) {
        setUpState(State.SUCCESS)
        fragmentOnSellBinding = FragmentOnSellBinding.bind(rootView)
        rvOnSelContent = fragmentOnSellBinding!!.rvOnSelContent
        rvOnSelContent.layoutManager = GridLayoutManager(context, 2)
        onSellPageContentAdapter = OnSellPageContentAdapter()
        rvOnSelContent.adapter = onSellPageContentAdapter
        rvOnSelContent.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = SizeUtils.dip2px(context!!, 2.5f)
                outRect.bottom = SizeUtils.dip2px(context!!, 2.5f)
                outRect.left = SizeUtils.dip2px(context!!, 2.5f)
                outRect.right = SizeUtils.dip2px(context!!, 2.5f)
            }
        })
        onSellRefreshLayout = fragmentOnSellBinding!!.onSellRefreshLayout
        onSellRefreshLayout.setEnableLoadmore(true)
        onSellRefreshLayout.setEnableRefresh(false)
        onSellRefreshLayout.setEnableOverScroll(true)
    }

    override fun initListener() {
        super.initListener()
        onSellRefreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                //去加载更多
                if (this@OnSellFragment::onSellPagePresenter.isInitialized) {
                    onSellPagePresenter.loadMore()
                }
            }
        })
        onSellPageContentAdapter.setOnOnSellContentItemClickListener(this)
    }

    private lateinit var onSellPagePresenter: OnSellPagePresenterImpl
    override fun initPresenter() {
        onSellPagePresenter = PresenterManager.getOnSellPagePresenter()
        onSellPagePresenter.registerViewCallback(this)
        onSellPagePresenter.getOnSellContent()
    }

    override fun onRetryClick() {
        onSellPagePresenter.getOnSellContent()
    }

    override fun release() {
        if (this::onSellPagePresenter.isInitialized) {
            onSellPagePresenter.unRegisterViewCallback(this)
        }
        if (fragmentOnSellBinding != null) {
            fragmentOnSellBinding = null
        }
        if (fragmentWithBarLayoutBinding != null) {
            fragmentWithBarLayoutBinding = null
        }
    }

    override fun onContentLoadedSuccess(result: OnSellContent) {
        setUpState(State.SUCCESS)
        LogUtils.d(this, "result ---> ${result.code}")
        if (this::onSellPageContentAdapter.isInitialized) {
            onSellPageContentAdapter.setData(result)
        }
    }

    override fun onMoreLoaded(moreResult: OnSellContent) {
        //获取
        setUpState(State.SUCCESS)
        ToastUtil.showToast("加载了${moreResult.data.tbk_dg_optimus_material_response.result_list.map_data.size}")
        onSellRefreshLayout.finishLoadmore()
        onSellPageContentAdapter.onMoreLoaded(moreResult)
    }

    override fun onMoreLoadedError() {
        onSellRefreshLayout.finishLoadmore()
        ToastUtil.showToast("网络错误，请稍后重试")
    }

    override fun onMoreLoadedEmpty() {
        onSellRefreshLayout.finishLoadmore()
        ToastUtil.showToast("没有更多内容")
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

    override fun onSellContentItemClick(item: IBaseInfo) {
        TicketUtil.toTicketPage(context!!,item)
    }
}