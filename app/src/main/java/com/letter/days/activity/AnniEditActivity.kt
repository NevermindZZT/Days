package com.letter.days.activity

import android.app.Activity
import android.app.startActivityForResult
import android.content.Intent
import android.content.getScreenHeight
import android.content.getScreenWidth
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
import android.content.toast
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File

private const val REQUEST_CODE_IMAGE = 1
private const val REQUEST_CODE_CROP = 2

private const val TAG = "AnniEditActivity"

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
                model.saveAnniversary {
                    toast(R.string.activity_anni_edit_toast_save_success)
                    finish()
                }
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
                val colors = resources.getStringArray(R.array.color_picker_values).map {
                    Color.parseColor(it)
                }
                val subColors = resources.getStringArray(R.array.color_picker_sub_values).map {
                    Color.parseColor(it)
                }
                val sub = mutableListOf<IntArray>()
                for (i in colors.indices) {
                    sub.add(subColors.slice((i * 7) until ((i + 1) * 7)).toIntArray())
                }
                MaterialDialog(this).show {
                    title(R.string.activity_anni_theme_dialog_title)
                    colorChooser(colors.toIntArray(),
                        subColors = sub.toTypedArray(),
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
            R.id.image_text -> {
                startActivityForResult(Intent.ACTION_OPEN_DOCUMENT, REQUEST_CODE_IMAGE) {
                    type = "image/*"
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "code: $requestCode, result: $resultCode")
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data?.data != null) {
                    startActivityForResult("com.android.camera.action.CROP", REQUEST_CODE_CROP) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        setDataAndType(data.data, "image/*")
                        putExtra("crop", "true")
                        putExtra("aspectY", getScreenHeight())
                        putExtra("aspectX", getScreenWidth())
                        putExtra("outputY", getScreenHeight())
                        putExtra("outputY", getScreenWidth())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val uri = Uri.fromFile(File(externalCacheDir!!.absolutePath,
                                "crop${System.currentTimeMillis()}"))
                            putExtra("output", uri)
                        }
                        putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                        putExtra("noFaceDetection", true)
                        putExtra("return-data", true)
                    }
                }
            }
            REQUEST_CODE_CROP -> {
                if (resultCode == Activity.RESULT_OK && data?.data != null) {
                    model.anniversary.value?.image = model.saveUri2File(data.data)
                    model.anniversary.notify()
                    Log.d(TAG, "file: ${model.anniversary.value?.image}")
                }
            }
        }
    }
}
