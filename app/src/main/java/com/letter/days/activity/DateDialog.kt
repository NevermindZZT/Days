package com.letter.days.activity

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.letter.days.R



/**
 * @author Letter(zhangkeqiang@ut.cn)
 * @version 1.0
 */
class DateDialog(context: Context, theme: Int) : Dialog(context, theme),
        CalendarView.OnCalendarSelectListener, View.OnClickListener,
        AdapterView.OnItemClickListener {

    private var calendarView: CalendarView? = null
    private var dayText: TextView? = null
    private var yearText: TextView? = null
    private var lunarText: TextView? = null
    private var positiveButton: Button? = null
    private var negativeButton: Button? = null
    private var yearLayout: LinearLayout? = null
    private var yearList: ListView? = null

    private var onDateSetListener: OnDateSetListener? = null

    private var calendar: Calendar? = null

    init {
        val view =  LayoutInflater.from(context).inflate(R.layout.dialog_date_pick, null)
        addContentView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

        calendarView = view.findViewById(R.id.calendar_view)
        dayText = view.findViewById(R.id.day_text)
        yearText = view.findViewById(R.id.year_text)
        lunarText = view.findViewById(R.id.lunar_text)
        positiveButton = view.findViewById(R.id.positive_button)
        negativeButton = view.findViewById(R.id.negative_button)
        yearLayout = view.findViewById(R.id.year_layout)
        yearList = view.findViewById(R.id.year_list)

        calendarView?.setOnCalendarSelectListener(this)
        dayText?.setOnClickListener(this)
        positiveButton?.setOnClickListener(this)
        negativeButton?.setOnClickListener(this)
        yearLayout?.setOnClickListener(this)

        val list: MutableList<String> = mutableListOf()
        for (i in 1970 until 2100) {
            list.add(i.toString())
        }
        val adapter = ArrayAdapter(context, R.layout.item_year_list, list)
        yearList?.adapter = adapter
        yearList?.onItemClickListener = this

        scrollToCurrent()
    }

    constructor(context: Context): this(context, R.style.Base_Theme_AppCompat_Light_Dialog)

    constructor(builder: Builder): this(builder.context, builder.getTheme()) {
        this.onDateSetListener = builder.getOnDateSetListener()
    }

    private fun freshDate(calendar: Calendar?) {
        val year: Int = calendar?.year ?: 2019
        val month: Int = calendar?.month ?: 1
        val day: Int = calendar?.day ?: 1

        val string = "${month}月${day}日"
        this.dayText?.text = string
        yearText?.text = year.toString()
        this.lunarText?.text = "${calendar?.lunar}"
    }

    private fun scrollToCurrent() {
        val curCalendar: java.util.Calendar = java.util.Calendar.getInstance()
        calendar = Calendar()
        calendar?.year = curCalendar.get(java.util.Calendar.YEAR)
        calendar?.month = curCalendar.get(java.util.Calendar.MONTH) + 1
        calendar?.day = curCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        calendarView?.scrollToCalendar(calendar?.year ?: 0,
                calendar?.month ?: 0, calendar?.day ?: 0)
    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        this.calendar = calendar
        freshDate(this.calendar)
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onClick(v: View?) {
        when (v) {
            dayText -> {
                scrollToCurrent()
            }
            positiveButton -> {
                this.onDateSetListener?.onDateSet(calendar?.year!!, calendar?.month!!, calendar?.day!!)
                dismiss()
            }
            negativeButton -> {
                dismiss()
            }
            yearLayout -> {
                yearList?.setSelection((calendar?.year ?: 1970) - 1970)
                yearList?.setItemChecked((calendar?.year ?: 1970) - 1970, true)
                yearList?.visibility = View.VISIBLE
                calendarView?.visibility = View.GONE
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        calendar?.year = position + 1970
        calendarView?.scrollToCalendar(calendar?.year ?: 0,
                calendar?.month ?: 0, calendar?.day ?: 0)
        yearList?.visibility = View.GONE
        calendarView?.visibility = View.VISIBLE
        yearText?.text = (position + 1970).toString()
    }

    class Builder(var context: Context) {
        private var onDateSetListener: OnDateSetListener? = null
        private var theme: Int = R.style.Base_Theme_AppCompat_Light_Dialog

        fun setOnDateSetListener(listener: OnDateSetListener): Builder {
            onDateSetListener = listener
            return this
        }

        fun getOnDateSetListener(): OnDateSetListener? {
            return onDateSetListener
        }

        fun setTheme(theme: Int): Builder {
            this.theme = theme
            return this
        }

        fun getTheme(): Int {
            return theme
        }

        fun create(): DateDialog {
            return DateDialog(this)
        }
    }

    interface OnDateSetListener {
        fun onDateSet(year: Int, month: Int, day: Int)
    }
}