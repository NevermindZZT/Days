package com.letter.days.dialog.datepicker

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.customview.customView
import com.haibin.calendarview.Calendar
import com.letter.days.dialog.datepicker.view.DatePickerView
import com.letter.days.utils.setTimeInMillis

/**
 * Material Dialog 日期选择器扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

typealias DateCallback = ((dialog: MaterialDialog, calendar: Calendar?) -> Unit)?

/**
 * 数据选择对话框
 * @receiver MaterialDialog 对话框
 * @param initialCalender Calendar 初始日期
 * @param selection Function2<[@kotlin.ParameterName] MaterialDialog, [@kotlin.ParameterName] Calendar?, Unit>? 确认回调
 * @return MaterialDialog 对话框
 */
fun MaterialDialog.datePicker(
    initialCalender: Calendar = Calendar().setTimeInMillis(),
    selection: DateCallback = null
): MaterialDialog {
    val datePickerView = DatePickerView(context)
//    datePickerView.calendar = initialCalender
    customView(view = datePickerView, noVerticalPadding = true)

    if (selection != null) {
        setActionButtonEnabled(WhichButton.POSITIVE, true)
        positiveButton {
            selection.invoke(this, datePickerView.calendar)
        }
    }
    return this
}
