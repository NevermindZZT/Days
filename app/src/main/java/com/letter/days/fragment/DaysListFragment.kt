package com.letter.days.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.letter.days.R
import com.letter.days.databinding.FragmentDaysListBinding

/**
 * 纪念日列表Fragment
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class DaysListFragment : Fragment() {

    private lateinit var binding: FragmentDaysListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaysListBinding.inflate(inflater)
        return binding.root
    }

}
