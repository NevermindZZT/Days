package com.letter.days.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class AnniFragmentPagerAdapter<T: Fragment>: FragmentPagerAdapter {

    var list: List<T>? = null

    constructor(fm: FragmentManager): super(fm)

    constructor(fm: FragmentManager, list: List<T>): super(fm) {
        this.list = list
    }

    override fun getItem(p0: Int): Fragment {
        return list?.get(p0)!!
    }

    override fun getCount(): Int {
        return list?.size ?: 0
    }
}