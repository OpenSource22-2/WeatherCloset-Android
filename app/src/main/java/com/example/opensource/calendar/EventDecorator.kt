package com.example.opensource.calendar

import android.app.Activity
import androidx.core.content.ContextCompat
import com.example.opensource.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class EventDecorator(dates: Collection<CalendarDay>, context: Activity?) : DayViewDecorator {
    private val drawable = ContextCompat.getDrawable(context!!, R.drawable.bg_calendar)
    private var dates : HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }
    init {
        // You can set background for Decorator via drawable here
    }
}