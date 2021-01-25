package edu.swpu.se2018.ylq.taobaounionsencond.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.Categories
import edu.swpu.se2018.ylq.taobaounionsencond.model.domain.CategoryData
import edu.swpu.se2018.ylq.taobaounionsencond.ui.fragments.HomePagerFragment
import edu.swpu.se2018.ylq.taobaounionsencond.utils.LogUtils

/**
 * <pre>
 *     author : YanLQ
 *     e-mail : yan15181802534@163.com
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class HomePagerAdapter (fm:FragmentManager):FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val categoryList = ArrayList<CategoryData>()

    override fun getCount(): Int {
        //LogUtils.d(this,"getCount:categoryList size -->${categoryList.size}")
        return categoryList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categoryList[position].title
    }
    override fun getItem(position: Int): Fragment {
        LogUtils.d(this,"getItem: position ---> $position")
        return HomePagerFragment.newInstance(categoryList[position])
    }

    fun setCategories(categories: Categories) {
        categoryList.clear()
        val data = categories.data
        categoryList.addAll(data)
        notifyDataSetChanged()
        LogUtils.d(this,"setCategories:categoryList -->${categoryList}")
    }
}