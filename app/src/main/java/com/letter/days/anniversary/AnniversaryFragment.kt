package com.letter.days.anniversary

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.letter.days.R

/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class AnniversaryFragment: Fragment() {

    var anniId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_anniversary_card, container, false)
        anniId = arguments?.getInt("anniId") ?: 0
        val anniversary = AnniUtils.getAnniversaryById(anniId)

        val anniText = view.findViewById<TextView>(R.id.anni_text)
        val anniDays = view.findViewById<TextView>(R.id.anni_days)
        val anniDate = view.findViewById<TextView>(R.id.anni_date)
        val anniType = view.findViewById<TextView>(R.id.anni_type)

        anniText.text = anniversary.text
        anniDays.text = anniversary.daysText
        anniDate.text = AnniUtils.getFormatDate("%d-%d-%d",
                "%d年 %s %s",
                anniversary.time,
                anniversary.isLunar)
        anniType.text = anniversary.typeText

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(anniId: Int): AnniversaryFragment {
            val anniversaryFragment = AnniversaryFragment()
            val bundle = Bundle()
            bundle.putInt("anniId", anniId)
            anniversaryFragment.arguments = bundle
            return anniversaryFragment
        }
    }
}