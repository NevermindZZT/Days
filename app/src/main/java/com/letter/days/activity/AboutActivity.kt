package com.letter.days.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import com.letter.days.BuildConfig
import com.letter.days.R
import com.letter.days.databinding.ActivityAboutBinding
import android.content.startActivity

/**
 * 关于活动
 * @property binding ActivityAboutBinding 视图绑定
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 设置Action Bar并使能home按钮 */
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initBinding()
    }

    private fun initBinding() {
        binding.versionText.text = BuildConfig.VERSION_NAME
        binding.githubText.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.activity_about_open_source_address))
            startActivity(intent)
        }
        binding.openSourceText.setOnClickListener {
            startActivity(OpenSourceActivity::class.java)
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
}
