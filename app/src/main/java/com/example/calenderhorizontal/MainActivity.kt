package com.example.calenderhorizontal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calenderhorizontal.databinding.ActivityMainBinding
import com.example.monthpicker.MonthAndYearPickerDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectDate.setOnClickListener {
            MonthAndYearPickerDialogFragment {
                binding.textSelectedDate.text = ""
                binding.horizontalCalenderView.invalidate(Pair(first = it.second, second = it.first.id-1))
            }.show(supportFragmentManager,"MonthPickerTag")
        }
        //Date change Listener when selecting a date
        binding.horizontalCalenderView.setDateChangeListener {
            binding.textSelectedDate.text = fetchSelectedDate(it)
        }


    }

    @SuppressLint("SimpleDateFormat")
    fun fetchSelectedDate(date: Date?): String = with(date ?: Date()) {
        SimpleDateFormat("dd-MMM-yyyy").format(this)
    }
}