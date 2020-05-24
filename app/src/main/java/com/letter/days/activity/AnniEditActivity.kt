package com.letter.days.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.letter.days.LetterApplication
import com.letter.days.R
import com.letter.days.database.AppDatabase
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.databinding.ActivityAnniEditBinding
import com.letter.days.viewmodel.AnniEditViewModel
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
class AnniEditActivity : AppCompatActivity() {

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

        /* Android O以上支持，设置浅色状态栏 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

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
                MainScope().launch {
                    val anniversary = model.anniversary.value
                    if (anniversary != null) {
                        AppDatabase.instance(this@AnniEditActivity)
                            .anniversaryDao().insert(anniversary)
                    }
                }
            }
        }
        return true
    }
}
