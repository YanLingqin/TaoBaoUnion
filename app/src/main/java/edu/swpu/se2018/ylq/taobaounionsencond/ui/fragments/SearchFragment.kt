package edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments

import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.base.BaseFragment
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentSearchBinding
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentSearchLayoutBinding
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Histories
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.ILinearItemInfo
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.SearchRecommendResult
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.SearchResult
import edu.swpu.se2018.ylq.taobaounionsencond.presenter.impl.SearchPresenterImpl
import edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter.LinearItemContentAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.ui.custom.TextFlowLayout
import edu.swpu.se2018.ylq.taobaounionsencond.utils.*
import edu.swpu.se2018.ylq.taobaounionsencond.view.ISearchViewCallback
import java.lang.Exception

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2020/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SearchFragment : BaseFragment(), ISearchViewCallback,
    LinearItemContentAdapter.OnListenItemClickListener, TextFlowLayout.OnFlowTextItemClickListener {
    override fun getSuccessViewResId() = R.layout.fragment_search

    private var fragmentSearchLayoutBinding: FragmentSearchLayoutBinding? = null

    override fun loadRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = inflater.inflate(
            R.layout.fragment_search_layout, container, false
        )
        fragmentSearchLayoutBinding = FragmentSearchLayoutBinding.bind(view)
        return view
    }

    private var fragmentSearchBinding: FragmentSearchBinding? = null
    private lateinit var searchRecommendView: TextFlowLayout
    private lateinit var searchHistoryView: TextFlowLayout
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var searchRecommendContainer: LinearLayout
    private lateinit var searchHistoryDelete: ImageView
    private lateinit var rvSearchResult: RecyclerView
    private lateinit var searchResultAdapter: LinearItemContentAdapter
    private lateinit var searchResultContainer: TwinklingRefreshLayout

    private lateinit var etSearchInput: EditText
    private lateinit var ivSearchClear: ImageView
    private lateinit var btnSearch: TextView
    override fun initView(rootView: View) {
        setUpState(State.SUCCESS)
        etSearchInput = fragmentSearchLayoutBinding!!.etSearchInput
        ivSearchClear = fragmentSearchLayoutBinding!!.ivSearchClear
        btnSearch = fragmentSearchLayoutBinding!!.btnSearch

        val frameLayout = rootView as FrameLayout
        fragmentSearchBinding = FragmentSearchBinding.bind(frameLayout.getChildAt(0))
        searchRecommendView = fragmentSearchBinding!!.searchRecommendView
        searchHistoryView = fragmentSearchBinding!!.searchHistoryView
        searchHistoryContainer = fragmentSearchBinding!!.searchHistoryContainer
        searchRecommendContainer = fragmentSearchBinding!!.searchRecommendContainer
        searchHistoryDelete = fragmentSearchBinding!!.searchHistoryDelete
        rvSearchResult = fragmentSearchBinding!!.rvSearchResult
        //设置布局管理器
        rvSearchResult.layoutManager = LinearLayoutManager(context)
        //设置适配器
        searchResultAdapter = LinearItemContentAdapter()
        rvSearchResult.adapter = searchResultAdapter
        rvSearchResult.addItemDecoration(object :
            RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = SizeUtils.dip2px(context!!, 2.5f)
                outRect.bottom = SizeUtils.dip2px(context!!, 2.5f)
            }
        })
        searchResultContainer = fragmentSearchBinding!!.searchResultContainer
        searchResultContainer.setEnableRefresh(false)
        searchResultContainer.setEnableLoadmore(true)
        searchResultContainer.setEnableOverScroll(true)

    }

    private var searchPresenter: SearchPresenterImpl? = null
    override fun initPresenter() {
        searchPresenter = PresenterManager.getSearchPresenter()
        searchPresenter!!.registerViewCallback(this)

        //获取推荐词
        searchPresenter!!.getRecommendWords()
        //searchPresenter!!.doSearch("毛衣")
        searchPresenter!!.getHistories()
    }


    override fun initListener() {
        super.initListener()
        //历史记录的事项的监听
        searchHistoryView.setOnFlowTextItemClickListener(this)

        //推荐热词的点击事件监听
        searchRecommendView.setOnFlowTextItemClickListener(this)

        //搜索点击的按钮的监听
        btnSearch.setOnClickListener {
            if(!TextUtils.isEmpty(etSearchInput.text)){
                val keyword = etSearchInput.text.toString().trim()
                toSearch(keyword)
                //searchPresenter!!.doSearch(keyword)
                KeyboardUtil.hide(context!!,it)
            }else{
                //收起软键盘
                KeyboardUtil.hide(context!!,it)
            }
        }

        //清除输入框的内容
        ivSearchClear.setOnClickListener {
            etSearchInput.setText("")
            //还需要回到历史记录页面
            switch2HistoryPage()
        }

        //监听输入框的内容变化
        etSearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //变化时进行通知
                //LogUtils.d(this@SearchFragment, "input text ===> ${s.toString()}")
                //如果长度不为0，那么显示删除按钮
                ivSearchClear.visibility = if (!TextUtils.isEmpty(s.toString())) {
                    //显示
                    View.VISIBLE
                } else {
                    //否则隐藏
                    View.GONE
                }
                //提示框也会跟着变化
                btnSearch.text = if (!TextUtils.isEmpty(s.toString().trim())){
                    "搜索"
                }else{
                    "取消"
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        //监听软键盘的搜索事件
        etSearchInput.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                LogUtils.d(this@SearchFragment, "actionId----> $actionId")
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val keyword = v!!.text.toString().trim()
                    if (TextUtils.isEmpty(keyword)) {
                        return false
                    }
                    //判断拿到的内容是否为空
                    LogUtils.d(this@SearchFragment, "keyword --->$keyword")
                    //发起搜索
                    toSearch(keyword)
                    //searchPresenter!!.doSearch(keyword)
                }
                return false
            }
        })

        //删除历史记录的监听
        searchHistoryDelete.setOnClickListener {
            if (searchPresenter != null) {
                searchPresenter!!.delHistories()
            }
        }

        searchResultAdapter.setOnListenItemClickListener(this)

        searchResultContainer.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                //去加载更多
                if (searchPresenter != null) {
                    searchPresenter!!.loadMore()
                }
            }
        })
    }

    //切换回历史记录页面
    private fun switch2HistoryPage() {
        if(searchPresenter!=null){
            searchPresenter!!.getHistories()
        }
        searchRecommendContainer.visibility =if (searchRecommendView.getContentSize()!=0) {
             View.VISIBLE
        }else{
            View.GONE
        }
        //隐藏内容
        searchResultContainer.visibility = View.GONE
    }


    override fun release() {
        searchPresenter?.unRegisterViewCallback(this)
        if (fragmentSearchBinding != null) {
            fragmentSearchBinding = null
        }

    }

    override fun onRetryClick() {
        LogUtils.d(this, "retry....")
        searchPresenter?.research()
    }

    override fun onHistoriesLoaded(histories: Histories?) {
        LogUtils.d(this, histories.toString())
        if (histories == null || histories.getHistories().isEmpty()) {
            searchHistoryContainer.visibility = View.GONE
        } else {
            searchHistoryContainer.visibility = View.VISIBLE
            searchHistoryView.setTextList(histories.getHistories())
        }

    }

    override fun onHistoriesDeleted() {
        ToastUtil.showToast("历史记录已删除")
        //更新历史记录
        if (searchPresenter != null) {
            searchPresenter!!.getHistories()
        }
    }

    override fun onSearchSuccess(result: SearchResult) {
        setUpState(State.SUCCESS)
        LogUtils.d(this, "result ----> $result")
        //隐藏掉历史记录和推荐
        searchRecommendContainer.visibility = View.GONE
        searchHistoryContainer.visibility = View.GONE
        //显示搜索界面
        searchResultContainer.visibility = View.VISIBLE
        //设置数据
        try {
            searchResultAdapter.setData(result.data.tbk_dg_material_optional_response.result_list.map_data)
        }catch (e:Exception){
            setUpState(State.EMPTY)
        }

    }

    override fun onMoreLoaded(result: SearchResult) {
        //加载更多的结果
        //拿到结果，添加到适配器中
        val moreData = result.data.tbk_dg_material_optional_response.result_list.map_data
        searchResultAdapter.addData(moreData)
        searchResultContainer.finishLoadmore()

        ToastUtil.showToast("加载到了${moreData.size}条记录")
    }

    override fun onMoreLoaderError() {
        ToastUtil.showToast("网络异常，请稍后重试")
        searchResultContainer.finishLoadmore()
    }

    override fun onMoreLoaderEmpty() {
        ToastUtil.showToast("没有更多数据")
        searchResultContainer.finishLoadmore()
    }

    override fun onRecommendWordsLoaded(recommendWords: List<SearchRecommendResult.Data>) {
        LogUtils.d(this, "recommendWords --->${recommendWords.size}")
        val recommendKeywords = ArrayList<String>()
        recommendWords.forEach {
            recommendKeywords.add(it.keyword)
        }
        if (recommendKeywords.isEmpty()) {
            searchRecommendContainer.visibility = View.GONE
        } else {
            searchRecommendContainer.visibility = View.VISIBLE
            searchRecommendView.setTextList(recommendKeywords)
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

    override fun onItemClick(item: ILinearItemInfo) {
        TicketUtil.toTicketPage(context!!, item)
    }

    override fun onFlowItemClick(text: String) {
        toSearch(text)
    }

    private fun toSearch(text: String) {
        if (searchPresenter != null) {
            rvSearchResult.scrollToPosition(0)
            searchPresenter!!.doSearch(text)
            //进行搜索的同时更新搜索框中的UI
            etSearchInput.setText(text)
            etSearchInput.setSelection(text.length)
            etSearchInput.isFocusable = true
            etSearchInput.requestFocus()
        }
    }
}