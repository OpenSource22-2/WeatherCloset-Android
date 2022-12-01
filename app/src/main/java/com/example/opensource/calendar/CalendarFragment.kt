package com.example.opensource.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.opensource.MainActivity
import com.example.opensource.R
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.*


class CalendarFragment : Fragment() {

    private lateinit var userDailyRecord: View
    private lateinit var binding: FragmentCalendarBinding
    private var dateList = arrayListOf<CalendarDay>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDailyRecord = view.findViewById(R.id.userDailyRecord)
        setCalendarView()
    }

    /*private fun setUserRecord(data: HomeRecordResponse.HomeRecordData) {
        Glide.with(requireContext())
            .load(data.imageUrl)
            .into(binding.userDailyRecord.ivImage)
        binding.userDailyRecord.tvTemperature.text = data.temperature.toString()
        binding.userDailyRecord.tvUsermemo.text = data.comment
        if (data.heart)
            binding.userDailyRecord.ivHeart.setImageResource(R.drawable.heart_full)
        else
            binding.userDailyRecord.ivHeart.setImageResource(R.drawable.heart_empty)
    }*/

    private fun setCalendarView(){

        var activity =  getActivity() as MainActivity

        // [미구현] 기록을 받아오는 코드
        dateList.add(CalendarDay.from(2022, 11, 11))
        dateList.add(CalendarDay.from(2022, 11, 23))
        dateList.add(CalendarDay.from(2022, 12, 1))
        dateList.add(CalendarDay.from(2023, 4, 30))

        // 먼슬리, 위클리 변환
        binding.icChange.setOnClickListener {
            if(binding.calendarView.calendarMode==CalendarMode.MONTHS){
                binding.calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit()
                if(dateList.contains(binding.calendarView.selectedDate))
                    userDailyRecord.setVisibility(View.VISIBLE)
            }
            else{
                binding.calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit()
                userDailyRecord.setVisibility(View.INVISIBLE)
            }
        }

        // 추가된 기록을 받아와 달력에 표시
        binding.calendarView.addDecorators(EventDecorator(dateList, activity))

        // 특정 날짜를 선택한 경우 이벤트 처리
        binding.calendarView.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                // 기록이 존재하는 경우, (유저 기록을 화면에 출력)
                if(dateList.contains(date)) {
                    widget.state().edit()
                        .setCalendarDisplayMode(CalendarMode.WEEKS)
                        .commit()

                    userDailyRecord.setVisibility(View.VISIBLE)
                    binding.clPlz.visibility = View.INVISIBLE
                }
                else {
                    userDailyRecord.setVisibility(View.INVISIBLE)
                    binding.clPlz.visibility = View.VISIBLE

                }
            }
        })
    }
}