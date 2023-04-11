package com.example.monthpicker

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.monthpicker.adapters.Month
import com.example.monthpicker.adapters.PagerAdapter
import com.example.monthpicker.databinding.DialogMonthYearPickerBinding
import com.example.monthpicker.models.CellType
import com.example.monthpicker.models.ViewPagerItem
import kotlinx.coroutines.*


class MonthAndYearPickerDialogFragment(val onSelectMonthAndYear:(item:Pair<Month,Int>)->Unit):DialogFragment(R.layout.dialog_month_year_picker) {

    private lateinit var binding: DialogMonthYearPickerBinding
    private var year = 2021
    private var month = Month(id = 1,"Jan", isSelected = true)

    private var viewPagerItemList = arrayListOf<ViewPagerItem>(
        ViewPagerItem(1, CellType.Month),
        ViewPagerItem(2,CellType.Year)
    )
    private lateinit var pagerAdapter: PagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogMonthYearPickerBinding.bind(view)
        binding.textYear.text = year.toString()
        binding.textMonth.text = month.month

        pagerAdapter = PagerAdapter(viewPagerItemList, onYearSelected = {
            binding.textYear.text = it.toString()
            year = it
        },{
            binding.textMonth.text = it.month
            month = it
            lifecycleScope.launch {
                delay(300)
                withContext(Dispatchers.Main){
                    binding.viewPager.currentItem = 1
                }
            }

        })
        binding.viewPager.adapter = pagerAdapter
        setOnClickListeners()
        pageChangeListener()

    }

    private fun pageChangeListener(){
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position==0){
                    selectMonth()
                }else{
                    selectYear()
                }
            }
        })
    }

    private fun setOnClickListeners(){
        binding.textCancel.setOnClickListener {
            dismiss()
        }

        binding.textOk.setOnClickListener {
            onSelectMonthAndYear(Pair(month,year))
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun selectMonth(){
        binding.textMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.textYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorGrey))
        binding.textDialogTitle.text = getString(R.string.select_month)
    }

    private fun selectYear(){
        binding.textMonth.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorGrey))
        binding.textYear.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        binding.textDialogTitle.text = getString(R.string.select_year)
    }
}