package com.letter.days.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.letter.days.R
import com.letter.days.databinding.ActivityMainBinding
import com.letter.presenter.ViewPresenter
import com.letter.utils.isDarkTheme
import com.letter.utils.startActivity

/**
 * 主活动
 * @property binding ActivityMainBinding 数据绑定
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class MainActivity
    : BaseActivity(),
    ViewPresenter
    , NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isDarkTheme()) {
            binding.mainLayout.collapsingToolbar.setExpandedTitleColor(0xff1a1a1a.toInt())
            binding.mainLayout.collapsingToolbar.setCollapsedTitleTextColor(0xff1a1a1a.toInt())
        } else {
            binding.mainLayout.barImage.setImageResource(R.drawable.bg_main_bar_night)
        }

        /* 设置Action Bar并使能home按钮 */
        setSupportActionBar(binding.mainLayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_main_menu)

        initBinding()
    }

    override fun onResume() {
        super.onResume()
        binding.navView.getHeaderView(0)
            ?.findViewById<TextView>(R.id.slogan_text)
            ?.text = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString("slogan", getString(R.string.setting_slogan_default))
    }

    /**
     * 初始化Data Binding
     */
    private fun initBinding() {
        binding.mainLayout.let {
            it.presenter = this
        }
        binding.navView.setNavigationItemSelectedListener(this)
    }

    /**
     * 菜单选项点击处理
     * @param item MenuItem 菜单选项
     * @return Boolean 事件是否被处理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    /**
     * View点击处理
     * @param v View? view
     */
    override fun onClick(v: View?) {
        when (v) {
            binding.mainLayout.fab -> {
                startActivity(AnniEditActivity::class.java) {
                    putExtra("anniId", -1)
                }
            }
        }
    }

    /**
     * navigation view 菜单点击处理
     * @param item MenuItem 菜单选项
     * @return Boolean 事件是否被处理
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_setting -> {
                startActivity(SettingActivity::class.java)
            }
            R.id.nav_about -> {
                startActivity(AboutActivity::class.java)
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }
}
