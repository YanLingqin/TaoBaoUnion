package edu.swpu.se2018.ylq.taobaounionsencond.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import edu.swpu.se2018.ylq.taobaounionsencond.R
import edu.swpu.se2018.ylq.taobaounionsencond.databinding.ActivityTestBinding
import edu.swpu.se2018.ylq.taobaounionsencond.ui.custom.TextFlowLayout
import edu.swpu.se2018.ylq.taobaounionsencond.utils.ToastUtil

class TestActivity : AppCompatActivity(), TextFlowLayout.OnFlowTextItemClickListener {
    val TAG = "TestActivity"
    private var testBinding: ActivityTestBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testBinding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(testBinding!!.root)
        initListener()
    }

    private fun initListener() {
        testBinding!!.testNavigationBar.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.test_home -> {
                    Log.d(TAG, "initListener: 首页点击")
                }
                R.id.test_search -> {
                    Log.d(TAG, "initListener: 搜索点击")
                }
                R.id.test_red_packet -> {
                    Log.d(TAG, "initListener: 特惠点击")
                }
                R.id.test_recommend -> {
                    Log.d(TAG, "initListener: 精选点击")
                }
            }
        }

        testBinding!!.textFlowLayout.setTextList(
            arrayListOf("1111111","22222222","33333333333",
        "4444444444","5555555555","6666")
        )
        testBinding!!.textFlowLayout.setOnFlowTextItemClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (testBinding != null) {
            testBinding = null
        }
    }

    override fun onFlowItemClick(text: String) {
        ToastUtil.showToast(text)
    }
}