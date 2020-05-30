package com.letter.days.activity

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.letter.days.LetterApplication
import com.letter.days.R
import com.letter.days.adapter.BindingViewAdapter
import com.letter.days.databinding.ActivityWidgetSettingBinding
import com.letter.days.viewmodel.DaysListViewModel
import com.letter.presenter.ItemClickPresenter
import com.letter.presenter.ViewPresenter
import kotlin.properties.Delegates

class WidgetSettingActivity : BaseActivity(), ItemClickPresenter, ViewPresenter {

    private lateinit var binding: ActivityWidgetSettingBinding
    private val model by lazy {
        ViewModelProvider
            .AndroidViewModelFactory(LetterApplication.instance())
            .create(DaysListViewModel::class.java)
    }

    private var widgetId by Delegates.notNull<Int>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWidgetSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 设置Action Bar并使能home按钮 */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setResult(RESULT_CANCELED)
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        initBinding()
        initModel()
    }

    private fun initBinding() {
        binding.let {
            it.presenter = this
        }
    }

    override fun onResume() {
        super.onResume()
        model.loadDays()
    }

    private fun initModel() {
        model.apply {
            daysList.observe(this@WidgetSettingActivity) {
                val adapter = BindingViewAdapter(
                    this@WidgetSettingActivity,
                    R.layout.layout_anniversary_item,
                    it,
                    this@WidgetSettingActivity
                )
                binding.daysListView.apply {
                    this.adapter = adapter
                    layoutManager = LinearLayoutManager(this@WidgetSettingActivity)
                }
            }
        }
    }

    /**
     * 菜单选项选择处理
     * @param item 被选中的选项
     * @return Boolean 动作是否被处理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onItemClick(adapter: Any, position: Int) {
        model.saveWidgetInfo(widgetId, model.daysList.value?.get(position)?.id ?: -1)
        val result = Intent()
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        setResult(RESULT_OK, result)
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.closest_text -> {
                model.saveWidgetInfo(widgetId, -1)
                val result = Intent()
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                setResult(RESULT_OK, result)
                finish()
            }
        }
    }
}
