package com.letter.days.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.list.listItems
import com.haibin.calendarview.Calendar
import com.letter.days.LetterApplication
import com.letter.days.R
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.databinding.ActivityAnniEditBinding
import com.letter.days.dialog.datepicker.datePicker
import com.letter.days.utils.notify
import com.letter.days.utils.setTimeInMillis
import com.letter.days.viewmodel.AnniEditViewModel
import com.letter.presenter.ViewPresenter
import com.letter.utils.toast
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * 纪念日编辑活动
 * @property binding ActivityAnniEditBinding 视图绑定
 * @property model AnniEditViewModel ViewModel
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class AnniEditActivity : BaseActivity(), ViewPresenter {

    private lateinit var binding: ActivityAnniEditBinding
    private val model by lazy {
        ViewModelProvider
            .AndroidViewModelFactory(LetterApplication.instance())
            .create(AnniEditViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnniEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 设置Action Bar并使能home按钮 */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initModel()
        initBinding()
    }

    /**
     * 初始化Data Binding
     */
    private fun initBinding() {
        binding.let {
            it.lifecycleOwner = this
            it.vm = model
            it.presenter = this
        }
    }

    /**
     * 初始化ViewModel
     */
    private fun initModel() {
        model.apply {
            MainScope().launch {
                val anniId = intent.getIntExtra("anniId", -1)
                anniversary.value = if (anniId == -1) AnniversaryEntity()
                    else AppDatabase.instance(this@AnniEditActivity)
                    .anniversaryDao().get(anniId)
            }
        }
    }

    /**
     * 创建菜单项
     * @param menu Menu 菜单
     * @return Boolean 是否以创建
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_anni_edit_toolbar, menu)
        return true
    }

    /**
     * 菜单选项点击处理
     * @param item MenuItem 菜单选项
     * @return Boolean 事件是否被处理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.save -> {
                model.saveAnniversary()
                toast(R.string.activity_anni_edit_toast_save_success)
                finish()
            }
        }
        return true
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.date_text -> {
                MaterialDialog(this).show {
                    datePicker(Calendar().setTimeInMillis(model.anniversary.value?.time)) {
                        dialog, calendar ->
                        model.anniversary.value?.time = calendar?.timeInMillis ?: 0
                        model.anniversary.notify()
                        dialog.dismiss()
                    }
                    positiveButton(R.string.dialog_positive_button)
                    negativeButton(R.string.dialog_negative_button)
                }
            }
            R.id.type_text -> {
                MaterialDialog(this).show {
                    title(R.string.activity_anni_type_dialog_title)
                    listItems(items = AnniversaryEntity.ANNI_TYPE_TEXT.toList()) {
                        dialog, index, _ ->
                        model.anniversary.value?.type = index
                        model.anniversary.notify()
                        dialog.dismiss()
                    }
                }
            }
            R.id.lunar_check -> {
                model.anniversary.notify()
            }
            R.id.theme_layout -> {
                val colors = mutableListOf<Int>()
                resources.getStringArray(R.array.color_picker_values).forEach {
                    colors.add(Color.parseColor(it))
                }
                MaterialDialog(this).show {
                    title(R.string.activity_anni_theme_dialog_title)
                    colorChooser(colors.toIntArray(),
                        initialSelection = model.anniversary.value?.color ?: 0,
                        allowCustomArgb = true,
                        showAlphaSelector = true) {
                        dialog, color ->
                        model.anniversary.value?.color = color
                        model.anniversary.notify()
                        dialog.dismiss()
                    }
                    positiveButton(R.string.dialog_positive_button)
                    negativeButton(R.string.dialog_negative_button)
                }
            }
        }
    }
}
