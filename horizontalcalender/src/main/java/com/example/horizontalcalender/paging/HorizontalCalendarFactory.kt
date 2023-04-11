package com.example.horizontalcalender.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.horizontalcalender.model.DayModel
import java.util.*

class HorizontalCalendarFactory() : DataSource.Factory<Int, DayModel>() {

    lateinit var dataSource: HorizontalCalendarSource
    var dataSourceLiveData: MutableLiveData<DataSource<Int, DayModel>> =
        MutableLiveData<DataSource<Int, DayModel>>()

    var date:Date = Calendar.getInstance().time

    override fun create(): DataSource<Int, DayModel> {
        dataSource =  HorizontalCalendarSource(today = date)
        dataSourceLiveData.postValue(dataSource)
        return  dataSource
    }

    fun refresh() {
        dataSourceLiveData.value?.invalidate()
    }
}