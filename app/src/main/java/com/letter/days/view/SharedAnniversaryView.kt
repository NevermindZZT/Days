package com.letter.days.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.drawToBitmap
import com.letter.days.database.entity.AnniversaryEntity
import com.letter.days.databinding.LayoutSharedAnniversaryBinding
import com.letter.days.utils.getDateString
import com.letter.days.utils.getDayText
import com.letter.days.utils.getNextTime
import com.letter.days.utils.getTypeText

/**
 * 分享纪念日视图
 * @constructor 构造器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class SharedAnniversaryView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0,
                          anniversary: AnniversaryEntity? = null)
    : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val binding = LayoutSharedAnniversaryBinding.inflate(LayoutInflater.from(context),
            this,
            false)
        addView(binding.root)

        binding.apply {
            nameText.text = anniversary?.name
            dayText.progress = 365 - (getNextTime(anniversary) ?: 0)
            dayText.strokeColor = anniversary?.color ?: 0
            dayText.text = anniversary?.getDayText()
            dateText.text = anniversary?.getDateString()
            typeText.text = anniversary?.getTypeText()
        }
    }

    /**
     * 创建View
     */
    private fun create() {
        measure(MeasureSpec.makeMeasureSpec(720, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(3840, MeasureSpec.AT_MOST))
        layout(0, 0, measuredWidth, measuredHeight)
    }

    /**
     * 获取View位图
     * @return Bitmap View生成的位图
     */
    fun getBitmap(): Bitmap {
        create()
        return drawToBitmap()
    }
}