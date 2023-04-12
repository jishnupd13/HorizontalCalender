package com.example.horizontalcalender.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.horizontalcalender.R
import com.example.horizontalcalender.databinding.CellDaysBinding
import com.example.horizontalcalender.model.DayModel
import java.text.SimpleDateFormat
import java.util.*

class HorizontalCalendarAdapter(
    private val onDateSelected:(date:Date)->Unit,
    private val onDateChanged:(date:Date)->Unit
) : PagedListAdapter<DayModel, HorizontalCalendarAdapter.HorizontalCalenderViewHolder>(DIFF_CALLBACK) {


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DayModel>() {
            override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
                return oldItem.equals( newItem)
            }

            override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }


    class HorizontalCalenderViewHolder(
        private val binding: CellDaysBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun onBind(dayModel: DayModel) = binding.apply {
            textDayName.text = fetchDateWeek(dayModel.date).take(3)
            textDayValue.text = fetchDay(dayModel.date)

            if(dayModel.isSelected){
                root.background = ContextCompat.getDrawable(root.context, R.drawable.bg_selected_date)
            }else{
                root.background = ContextCompat.getDrawable(root.context, R.drawable.bg_unselected_date)
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun fetchDateWeek(date: Date?): String = with(date ?: Date()) {
            SimpleDateFormat("EEE").format(this)
        }

        @SuppressLint("SimpleDateFormat")
        fun fetchDay(date: Date?): String = with(date ?: Date()) {
            SimpleDateFormat("dd").format(this)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalCalenderViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CellDaysBinding.inflate(layoutInflater, parent, false)
        return HorizontalCalenderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HorizontalCalenderViewHolder, position: Int) {
        getItem(position)?.let { day ->
            onDateChanged(day.date)
            holder.onBind(day)
            holder.itemView.setOnClickListener {
                onSelect(day)
            }
        }
    }

    private fun onSelect(day: DayModel) {
        currentList?.binarySearch {
            it.date.compareTo(day.date)
        }?.let { currentPosition ->
            onSelect(currentPosition)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSelect(position: Int) {
        currentList?.let {
            it.forEach { day -> day.isSelected = false }
            getItem(position)?.isSelected = true
            notifyDataSetChanged()
            getItem(position)?.date?.let { it1 -> onDateSelected.invoke(it1) }
        }
    }
}