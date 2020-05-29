package com.letter.days.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * View Pager适配器
 * @param T: Fragment Fragment
 * @property list List<T>? fragment列表
 * @constructor 构造器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class ViewPagerAdapter<T: Fragment>
constructor(fragmentManager: FragmentManager, private val list:  List<T>? = null)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return list?.get(position)!!
    }

    override fun getCount(): Int {
        return list?.size ?: 0
    }
}