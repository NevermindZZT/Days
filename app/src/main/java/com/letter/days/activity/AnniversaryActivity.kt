package com.letter.days.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.*
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.letter.days.R
import com.letter.days.adapter.ViewPagerAdapter
import com.letter.days.databinding.ActivityAnniversaryBinding
import com.letter.days.transformer.AnniViewPagerTransformer
import com.letter.days.viewmodel.AnniversaryViewModel
import com.letter.utils.startActivity

/**
 * 纪念日详情界面
 * @property binding ActivityAnniversaryBinding 视图绑定
 * @property model AnniversaryViewModel view model
 * @property onPageChangeListener <no name provided> 页面改变监听
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class AnniversaryActivity : BaseActivity() {

    private lateinit var binding: ActivityAnniversaryBinding
    private val model by lazy {
        ViewModelProvider(this).get(AnniversaryViewModel::class.java)
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            model.currentPosition.value = position
        }

        override fun onPageSelected(position: Int) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnniversaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 设置Action Bar并使能home按钮 */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initBinding()
        initModel()
    }

    override fun onResume() {
        super.onResume()
        model.loadDays()
    }

    /**
     * 初始化视图绑定
     */
    private fun initBinding() {
    }

    /**
     * 初始化view model
     */
    private fun initModel() {
        model.apply {
            fragmentList.observe(this@AnniversaryActivity) {
                binding.viewPager.apply {
                    adapter = ViewPagerAdapter(supportFragmentManager, it)
                    setCurrentItem(
                        model.currentPosition.value ?: model.getFragmentPosition(intent.getIntExtra("anniId", -1)),
                        true)
                    setPageTransformer(false, AnniViewPagerTransformer())
                    addOnPageChangeListener(onPageChangeListener)
                }
            }
        }
    }

    /**
     * 创建菜单那
     * @param menu Menu 菜单
     * @return Boolean 是否创建
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_anniversary_toolbar, menu)
        return true
    }

    /**
     * 菜单选项选择处理
     * @param item 被选中的选项
     * @return Boolean 动作是否被处理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.delete -> {
                MaterialDialog(this).show {
                    message(R.string.activity_anniversary_delete_dialog_message)
                    positiveButton(R.string.dialog_positive_button) {
                        model.deleteCurrent()
                        it.dismiss()
                        this@AnniversaryActivity.finish()
                    }
                    negativeButton(R.string.dialog_negative_button)
                }
            }
            R.id.edit -> {
                startActivity(AnniEditActivity::class.java) {
                    putExtra("anniId", model.getCurrentAnniversaryId())
                }
            }
        }
        return true
    }
}
