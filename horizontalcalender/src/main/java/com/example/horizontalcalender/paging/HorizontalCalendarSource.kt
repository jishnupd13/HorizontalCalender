package com.example.horizontalcalender.paging

import androidx.paging.PageKeyedDataSource
import com.example.horizontalcalender.model.DayModel
import java.util.*

class HorizontalCalendarSource(val today:Date): PageKeyedDataSource<Int, DayModel>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DayModel>
    ) {
        val day = DayModel(today,true)
        callback.onResult(mutableListOf(day), -1, 1)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DayModel>) {

        val cal = Calendar.getInstance()
        cal.time = today
        cal.add(Calendar.DATE, params.key)

        val previousDay = cal.time
        val day = DayModel(
            isSelected = false,
            date = previousDay
        )
        callback.onResult(mutableListOf(day), params.key - 1)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DayModel>) {

        val cal = Calendar.getInstance()
        cal.time = today
        cal.add(Calendar.DATE, params.key)

        val nextDay = cal.time
        val day = DayModel(
            isSelected = false,
            date = nextDay
        )
        callback.onResult(mutableListOf(day), params.key + 1)
    }
}