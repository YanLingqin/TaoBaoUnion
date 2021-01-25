package edu.swpu.se2018.ylq.taobaounionsencond.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.FragmentErrorViewBinding
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2020/12/12
 *     desc   :  因为大多数的fragment都实现了相同的办法，可以抽取到该类里面
 *     version: 1.0
 * </pre>
 */
abstract class BaseFragment : Fragment() {

    //定义的页面状态
    enum class State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private var currentState = State.NONE
    private lateinit var mBaseContainer: FrameLayout

    private var errorViewBinding: FragmentErrorViewBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = loadRootView(inflater, container)
        mBaseContainer = rootView!!.findViewById(R.id.base_container)
        loadStatesView(inflater, container)
        errorViewBinding = FragmentErrorViewBinding.bind(errorView)
        initView(mBaseContainer)
        initListener()
        initPresenter()
        loadData()
        return rootView
    }

    open fun initListener() {
        errorViewBinding!!.btnNetworkError.setOnClickListener {
            //点击重新加载内容
            LogUtils.d(this, "retry....")
            onRetryClick()
        }
    }

    //处理重试请求
    open fun onRetryClick() {

    }

    open fun loadRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.base_fragment_layout, container, false)
    }

    private lateinit var successView: View
    private lateinit var loadingView: View
    private lateinit var errorView: View
    private lateinit var emptyView: View

    //加载不同状态的view
    private fun loadStatesView(inflater: LayoutInflater, container: ViewGroup?) {
        //成功的View
        successView = loadSuccessView(inflater, container)
        mBaseContainer.addView(successView)

        //loading的view
        loadingView = loadLoadingView(inflater, container)
        mBaseContainer.addView(loadingView)
        //错误页面
        errorView = loadErrorView(inflater, container)
        mBaseContainer.addView(errorView)
        emptyView = loadEmptyView(inflater, container)
        mBaseContainer.addView(emptyView)
        setUpState(State.NONE)
    }

    open fun loadErrorView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_error_view, container, false)
    }

    open fun loadEmptyView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_empty_view, container, false)
    }

    //子类通过该方法切换状态页面
    fun setUpState(state: State) {
        this.currentState = state
        successView.visibility = if (currentState == State.SUCCESS) {
            View.VISIBLE
        } else {
            View.GONE
        }
        loadingView.visibility = if (currentState == State.LOADING) {
            View.VISIBLE
        } else {
            View.GONE
        }
        errorView.visibility = if (currentState == State.ERROR) {
            View.VISIBLE
        } else {
            View.GONE
        }
        emptyView.visibility = if (currentState == State.EMPTY) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    //支持对加载页面的重写
    open fun loadLoadingView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_loading_view, container, false)
    }

    //初始化控件
    open fun initView(rootView: View) {

    }


    override fun onDestroy() {
        super.onDestroy()
        release()
        if (errorViewBinding != null) {
            errorViewBinding = null
        }
    }

    //释放资源
    open fun release() {

    }

    //创建Presenter
    open fun initPresenter() {

    }

    //加载数据
    open fun loadData() {

    }

    private fun loadSuccessView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        val resId = getSuccessViewResId()
        return inflater.inflate(resId, container, false)
    }

    abstract fun getSuccessViewResId(): Int
}