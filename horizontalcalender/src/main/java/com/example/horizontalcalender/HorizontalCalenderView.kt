package com.example.horizontalcalender

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.paging.toLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.horizontalcalender.adapter.HorizontalCalendarAdapter
import com.example.horizontalcalender.paging.HorizontalCalendarFactory
import java.text.SimpleDateFormat
import java.util.*


class HorizontalCalenderView(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {

    private var  recyclerviewHorizontalCalender:RecyclerView
    private var dataSource = HorizontalCalendarFactory()
    private var horizontalCalendarAdapter:HorizontalCalendarAdapter
    private var  onDateClick: ((Date)->Unit)? =null
    private var centerSmoothScroller:CenterSmoothScroller?=null
    private var textCurrentDate:TextView

    init {
        centerSmoothScroller = CenterSmoothScroller(context)
        inflate(context, R.layout.layout_horizontal_calender, this)
        recyclerviewHorizontalCalender = findViewById(R.id.recyclerViewHorizontalCalender)
        textCurrentDate = findViewById(R.id.textDate)
        textCurrentDate.text = fetchCurrentDate(Calendar.getInstance().time)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.HorizontalCalenderView)
        horizontalCalendarAdapter = HorizontalCalendarAdapter({
            onDateClick?.let { it1 -> it1(it) }
        },{
            textCurrentDate.text = fetchCurrentDate(it)
        })
        recyclerviewHorizontalCalender.addItemDecoration(HorizontalCalenderViewItemDecorator(8))
        recyclerviewHorizontalCalender.apply {
            adapter = horizontalCalendarAdapter
        }

        observeHorizontalCalenderView()
        attributes.recycle()
    }

    private fun observeHorizontalCalenderView(){
        dataSource.toLiveData(30).observe(context.getLifecycleOwner()){
            horizontalCalendarAdapter.submitList(it)
        }
    }

    fun setDateChangeListener( onDateClick:(Date)->Unit){
      this.onDateClick = onDateClick
    }

    //first component will be year and second component will be the month
    fun invalidate( datePair:Pair<Int,Int>){
        val year = datePair.first
        val month = datePair.second
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH,month)
        cal.set(Calendar.YEAR,year)
        cal.set(Calendar.DAY_OF_MONTH,1)
        dataSource.date = cal.time
        horizontalCalendarAdapter.submitList(null)
        dataSource.refresh()
    }

    @SuppressLint("SimpleDateFormat")
    fun fetchCurrentDate(date: Date?): String = with(date ?: Date()) {
        SimpleDateFormat("MMM yyyy").format(this)
    }
}

class HorizontalCalenderViewItemDecorator(
    private val horizontalPadding:Int
): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = horizontalPadding
        outRect.right = horizontalPadding
    }
}

class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        val boxCenter = (boxEnd - boxStart) / 2
        val boxScreenCenter = boxStart + boxCenter
        val viewCenter = (viewEnd - viewStart) / 2
        val viewScreenCenter = viewStart + viewCenter
        return boxScreenCenter - viewScreenCenter
    }
}

