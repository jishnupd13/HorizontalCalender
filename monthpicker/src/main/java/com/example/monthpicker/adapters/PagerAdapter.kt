package com.example.monthpicker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.monthpicker.databinding.CellMonthPagerItemBinding
import com.example.monthpicker.databinding.CellYearItemBinding
import com.example.monthpicker.models.CellType
import com.example.monthpicker.models.ViewPagerItem

class PagerAdapter(
    val list: List<ViewPagerItem>,
    val onYearSelected:(year:Int)->Unit,
    val onMonthSelected:(month:Month)->Unit
): Adapter<RecyclerView.ViewHolder>() {

    class MonthItemViewHolder(val binding: CellMonthPagerItemBinding) : RecyclerView.ViewHolder(binding.root)
    class YearItemViewHolder(val binding: CellYearItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0-> MonthItemViewHolder(CellMonthPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            1-> YearItemViewHolder(CellYearItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            0->{
                val binding = (holder as MonthItemViewHolder).binding
                val monthAdapter = MonthAdapter(onMonthSelected = {
                    onMonthSelected.invoke(it)
                })
                binding.recyclerviewMonth.adapter = monthAdapter
            }

            1->{
                val binding = (holder as YearItemViewHolder).binding
                val yearAdapter = YearAdapter(onYearSelected = {
                    onYearSelected(it)
                })
                binding.recyclerviewYear.adapter = yearAdapter
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position].cellType){
            CellType.Month-> CellType.Month.ordinal
            CellType.Year-> CellType.Year.ordinal
        }
    }

}