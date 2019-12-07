package com.letter.days.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class AnniFragmentPagerAdapter<T: androidx.fragment.app.Fragment>: androidx.fragment.app.FragmentPagerAdapter {

    var list: List<T>? = null

    constructor(fm: androidx.fragment.app.FragmentManager): super(fm)

    constructor(fm: androidx.fragment.app.FragmentManager, list: List<T>): super(fm) {
        this.list = list
    }

    override fun getItem(p0: Int): androidx.fragment.app.Fragment {
        return list?.get(p0)!!
    }

    override fun getCount(): Int {
        return list?.size ?: 0
    }
}