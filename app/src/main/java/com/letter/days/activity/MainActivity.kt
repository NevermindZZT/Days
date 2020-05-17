package com.letter.days.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.letter.days.R
import com.letter.days.databinding.ActivityMainBinding
import com.letter.presenter.ViewPresenter
import com.letter.utils.toast

/**
 * 主活动
 * @property binding ActivityMainBinding 数据绑定
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class MainActivity : AppCompatActivity(), ViewPresenter {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Android O以上支持，设置浅色状态栏 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        /* 设置Action Bar并使能home按钮 */
        setSupportActionBar(binding.mainLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_main_menu)

        initBinding()
    }

    /**
     * 初始化Data Binding
     */
    private fun initBinding() {
        binding.mainLayout.let {
            it.presenter = this
        }
    }

    /**
     * View点击处理
     * @param v View? view
     */
    override fun onClick(v: View?) {
        when (v) {
            binding.mainLayout.fab -> toast("fab")
        }
    }
}
