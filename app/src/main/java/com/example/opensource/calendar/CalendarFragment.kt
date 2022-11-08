package com.example.opensource.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.opensource.MainActivity
import com.example.opensource.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener


class CalendarFragment : Fragment() {

    private lateinit var widget: MaterialCalendarView
    private lateinit var userDailyRecord: View
    private var dateList = arrayListOf<CalendarDay>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDailyRecord = view!!.findViewById(R.id.userDailyRecord)
        widget = view!!.findViewById(R.id.calendarView)

        setCalendarView()
    }

    private fun setCalendarView(){

        var activity =  getActivity() as MainActivity

        // [미구현] 기록을 받아오는 코드
        dateList.add(CalendarDay.from(2022, 11, 11))
        dateList.add(CalendarDay.from(2022, 11, 23))
        dateList.add(CalendarDay.from(2022, 12, 1))

        // 추가된 기록을 받아와 달력에 표시
        widget.addDecorators(EventDecorator(dateList, activity))

        // 특정 날짜를 선택한 경우 이벤트 처리
        widget.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                // 기록이 존재하는 경우, 유저 기록을 화면에 출력
                if(dateList.contains(date)) {
                    userDailyRecord.setVisibility(View.VISIBLE)
                }
                else {
                    userDailyRecord.setVisibility(View.INVISIBLE)
                }
            }
        })

        // 선택한 달이 바뀌는 경우 유저 기록 화면 숨기기
        widget.setOnMonthChangedListener(object: OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
                userDailyRecord.setVisibility(View.INVISIBLE)
            }
        })
    }
}