package com.letter.days.dialog.datepicker.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.letter.days.R
import com.letter.days.databinding.LayoutDatePickerBinding
import com.letter.days.utils.setTimeInMillis

/**
 * 日期选择View
 * @property binding LayoutDatePickerBinding 视图绑定
 * @property calendar Calendar? 日历
 * @property yearList MutableList<String> 年份列表
 * @constructor 构造一个日期选择视图
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class DatePickerView(context: Context):
    LinearLayout(context),
    View.OnClickListener,
    CalendarView.OnCalendarSelectListener,
    AdapterView.OnItemClickListener {

    private var binding: LayoutDatePickerBinding =
        LayoutDatePickerBinding.inflate(LayoutInflater.from(context), this, true)

    var calendar: Calendar? = Calendar().setTimeInMillis()
    set(value) {
        field = value
        if (value != null) {
            freshDate()
        }
    }

    private val yearList by lazy {
        val list: MutableList<String> = mutableListOf()
        for (i in 1970 until 2100) {
            list.add(i.toString())
        }
        list
    }

    init {
        binding.dayText.setOnClickListener(this)
        binding.yearLayout.setOnClickListener(this)
        binding.calendarView.setOnCalendarSelectListener(this)

        val adapter = ArrayAdapter(context, R.layout.layout_date_picker_year_list_item, yearList)
        binding.yearList.adapter = adapter
        binding.yearList.onItemClickListener = this

        scrollToCurrent()
    }

    /**
     * 刷新数据显示
     */
    private fun freshDate() {
        val dayText = "${calendar?.month}月${calendar?.day}日"
        binding.dayText.text = dayText
        binding.yearText.text = calendar?.year.toString()
        binding.lunarText.text = calendar?.lunar
    }

    /**
     * 跳转到当前日期
     */
    private fun scrollToCurrent() {
        val curCalendar: java.util.Calendar = java.util.Calendar.getInstance()
        calendar = Calendar()
        calendar?.year = curCalendar.get(java.util.Calendar.YEAR)
        calendar?.month = curCalendar.get(java.util.Calendar.MONTH) + 1
        calendar?.day = curCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        binding.calendarView.scrollToCalendar(calendar?.year ?: 0,
            calendar?.month ?: 0, calendar?.day ?: 0)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.day_text -> {
                scrollToCurrent()
            }
            R.id.year_layout -> {
                binding.yearList.setSelection((calendar?.year ?: 1970) - 1970)
                binding.yearList.setItemChecked((calendar?.year ?: 1970) - 1970, true)
                binding.calendarView.visibility = View.GONE
                binding.yearList.visibility = View.VISIBLE
            }
        }
    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        this.calendar = calendar
        freshDate()
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        calendar?.year = position + 1970
        binding.calendarView.scrollToCalendar(calendar?.year ?: 0,
            calendar?.month ?: 0, calendar?.day ?: 0)
        binding.calendarView.visibility = View.VISIBLE
        binding.yearList.visibility = View.GONE
        freshDate()
    }
}