package com.letter.days.fragment

import android.animation.ObjectAnimator
import android.animation.start
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModelProvider
import com.letter.days.databinding.FragmentAnniversaryBinding
import com.letter.days.utils.*
import com.letter.days.viewmodel.AnniversaryViewModel
import kotlin.properties.Delegates

private const val ANNI_POSITION= "anni_position"

/**
 * 纪念日Fragment
 * @property anniPosition Int 纪念日索引
 * @property binding FragmentAnniversaryBinding 视图绑定
 * @property model AnniversaryViewModel view model
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class AnniversaryFragment : Fragment() {

    private var anniPosition: Int = 0
    private lateinit var binding: FragmentAnniversaryBinding
    private val model by lazy {
        ViewModelProvider(activity as AppCompatActivity).get(AnniversaryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            anniPosition = it.getInt(ANNI_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnniversaryBinding.inflate(inflater)
        initBinding()
        return binding.root
    }

    private fun initBinding() {
        binding.let {
            it.lifecycleOwner = viewLifecycleOwner
            it.anniversary = model.daysList.value?.get(anniPosition)
        }
        binding.apply {
            nameText.text = model.daysList.value?.get(anniPosition)?.name
            val progress = 365 - (getNextTime(model.daysList.value?.get(anniPosition)) ?: 0)
            ObjectAnimator.ofInt(dayText, "progress", 0, progress)
                .start {
                    duration = 500
                    interpolator = AccelerateDecelerateInterpolator()
                }
            dayText.strokeColor = model.daysList.value?.get(anniPosition)?.color ?: 0
            dayText.text = model.daysList.value?.get(anniPosition)?.getDayText()
            dateText.text = model.daysList.value?.get(anniPosition)?.getDateString()
            typeText.text = model.daysList.value?.get(anniPosition)?.getTypeText()
        }
    }

    /**
     * 刷新进度
     */
    fun freshProgress() {
        ObjectAnimator.ofInt(binding.dayText,
            "progress",
            0,
            365 - (getNextTime(model.daysList.value?.get(anniPosition)) ?: 0))
            .start {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
            }
    }

    companion object {

        /**
         * 新实例
         * @param anniPosition Int 纪念日索引
         * @return AnniversaryFragment AnniversaryFragment
         */
        fun newInstance(anniPosition: Int) =
            AnniversaryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANNI_POSITION, anniPosition)
                }
            }
    }
}
