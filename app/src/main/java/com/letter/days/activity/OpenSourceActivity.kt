package com.letter.days.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.letter.days.LetterApplication
import com.letter.days.R
import com.letter.days.adapter.BindingViewAdapter
import com.letter.days.databinding.ActivityOpenSourceBinding
import com.letter.days.viewmodel.OpenSourceViewModel
import com.letter.presenter.ItemClickPresenter

/**
 * 开源相关活动
 * @property binding ActivityOpenSourceBinding 视图绑定
 * @property model OpenSourceViewModel viewmodel
 * @property adapter BindingViewAdapter<(com.letter.days.data.bean.OpenSourceBean..com.letter.days.data.bean.OpenSourceBean?)>
 *           列表适配器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class OpenSourceActivity : BaseActivity(), ItemClickPresenter {

    private lateinit var binding: ActivityOpenSourceBinding
    private val model by lazy {
        ViewModelProvider
            .AndroidViewModelFactory(LetterApplication.instance()).create(OpenSourceViewModel::class.java)
    }
    private val adapter by lazy {
        BindingViewAdapter(this,
            R.layout.layout_open_source_item,
            model.openSourceList,
            itemClickPresenter = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 设置Action Bar并使能home按钮 */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initBinding()
    }

    /**
     * 初始化视图绑定
     */
    private fun initBinding() {
        binding.openSourceView.apply {
            adapter = this@OpenSourceActivity.adapter
            layoutManager = LinearLayoutManager(this@OpenSourceActivity)
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
     * 列表项点击处理
     * @param adapter Any adapter
     * @param position Int 点击位置
     */
    override fun onItemClick(adapter: Any, position: Int) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(model.openSourceList[position].url)
        startActivity(intent)
    }
}
