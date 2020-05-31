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
import com.letter.utils.sendBroadcast
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

    override fun onResume() {
        super.onResume()
        model.loadDays()
    }

    /**
     * 初始化试图绑定
     */
    private fun initBinding() {
        binding.let {
            it.presenter = this
        }
    }

    /**
     * 初始化view model
     */
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
     * 包括widget数据
     * @param widgetId Int widget id
     * @param anniId Int 纪念日id
     */
    private fun saveWidgetInfo(widgetId: Int, anniId: Int) {
        model.saveWidgetInfo(widgetId, anniId) {
            sendBroadcast("android.appwidget.action.APPWIDGET_UPDATE") {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }

            val result = Intent()
            result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            setResult(RESULT_OK, result)
            finish()
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

    /**
     * item点击处理
     * @param adapter Any 适配器
     * @param position Int item位置
     */
    override fun onItemClick(adapter: Any, position: Int) {
        saveWidgetInfo(widgetId, model.daysList.value?.get(position)?.id ?: -1)
    }

    /**
     * 视图点击处理
     * @param v View? 视图
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.closest_text -> {
                saveWidgetInfo(widgetId, -1)
            }
        }
    }
}
