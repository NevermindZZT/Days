package com.letter.days.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.letter.days.adapter.BindingViewAdapter
import com.letter.days.LetterApplication
import com.letter.days.R
import com.letter.days.activity.AnniversaryActivity
import com.letter.days.databinding.FragmentDaysListBinding
import com.letter.days.viewmodel.DaysListViewModel
import com.letter.presenter.ItemClickPresenter
import android.content.startActivity

/**
 * 纪念日列表Fragment
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class DaysListFragment : Fragment(), ItemClickPresenter {

    private lateinit var binding: FragmentDaysListBinding
    private val model by lazy {
        ViewModelProvider
            .AndroidViewModelFactory(LetterApplication.instance())
            .create(DaysListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaysListBinding.inflate(inflater)
        initModel()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        model.loadDays()
    }

    private fun initModel() {
        model.apply {
            daysList.observe(this@DaysListFragment.viewLifecycleOwner) {
                val adapter = BindingViewAdapter(
                    this@DaysListFragment.requireContext(),
                    R.layout.layout_anniversary_item,
                    it,
                    model,
                    this@DaysListFragment
                )
                binding.daysListView.apply {
                    this.adapter = adapter
                    layoutManager = LinearLayoutManager(this@DaysListFragment.requireContext())
                }
            }
        }
    }

    override fun onItemClick(adapter: Any, position: Int) {
        context?.startActivity(AnniversaryActivity::class.java) {
            putExtra("anniId", model.daysList.value?.get(position)?.id ?: -1)
        }
    }
}

