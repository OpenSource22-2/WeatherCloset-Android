package com.example.opensource.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.opensource.R
import com.example.opensource.Secret
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.CalendarRecordResponse
import com.example.opensource.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CalendarFragment : Fragment() {

    private lateinit var userDailyRecord: View
    private lateinit var binding: FragmentCalendarBinding
    val recordList = arrayListOf<CalendarRecordResponse.CalendarRecordData>()
    val dateList = arrayListOf<CalendarDay>()

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

        initCalendarView()
        setCalendarView()
    }

    private fun setUserRecord(data: CalendarRecordResponse.CalendarRecordData) {
        Glide.with(requireContext())
            .load(data.imageUrl)
            .fitCenter()
            .into(binding.userDailyRecord.ivRecord)
        binding.userDailyRecord.tvDate.text = data.recordDate
        binding.userDailyRecord.tvTemperature.text = data.temperature.toString() + "ºC"
        binding.userDailyRecord.tvMemo.text = data.comment
        if (data.heart)
            binding.userDailyRecord.ivHeart.setImageResource(R.drawable.heart_white_line)
        else
            binding.userDailyRecord.ivHeart.setImageResource(R.drawable.heart_empty)
    }

    private fun setData(year: Int, month: Int){
        val call: Call<CalendarRecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getCalendarRecord(Secret.memberId, year, month)

        call.enqueue(object : Callback<CalendarRecordResponse> {
            override fun onResponse(
                call: Call<CalendarRecordResponse>,
                response: Response<CalendarRecordResponse>
            ) {
                if (response.isSuccessful) {

                    // 레코드 받아오기
                    recordList.addAll(response.body()!!.data)

                    // 레코드에서 날짜만 가공하여 추출
                    getDate(dateList, recordList)

                    binding.calendarView.addDecorators(EventDecorator(dateList, activity))

                } else {
                    // Log.e(CalendarFragment.TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<CalendarRecordResponse>, t: Throwable) {
                // Log.d(CalendarFragment.TAG, "onFailure: $t")
            }
        })
    }

    private fun getDate(dateList: ArrayList<CalendarDay>, data: List<CalendarRecordResponse.CalendarRecordData>){
        for(date in data){
            val mDate = date.recordDate.replace(" ", "")
            val toCalendarDay = mDate.split(".")
            dateList.add(CalendarDay.from(toCalendarDay[0].toInt(), toCalendarDay[1].toInt(), toCalendarDay[2].toInt()))
        }
    }

    private fun initCalendarView() {
        setData(binding.calendarView.currentDate.year, binding.calendarView.currentDate.month)
    }

    private fun setCalendarView(){

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

        // 특정 날짜를 선택한 경우 이벤트 처리
        binding.calendarView.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {

                val mdate = String.format("%d. %02d. %02d", date.year, date.month, date.day)
                val record = recordList.find { it.recordDate.contains(mdate) }

                // 기록이 존재하는 경우 화면에 표시
                if(record!=null){

                    widget.state().edit()
                        .setCalendarDisplayMode(CalendarMode.WEEKS)
                        .commit()

                    setUserRecord(record)

                    userDailyRecord.setVisibility(View.VISIBLE)
                    binding.clPlz.visibility = View.INVISIBLE
                }
                else{
                    userDailyRecord.setVisibility(View.INVISIBLE)
                    binding.clPlz.visibility = View.VISIBLE
                }
            }
        })

        // 달을 바꾸는 경우
        binding.calendarView.setOnMonthChangedListener(object: OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
                // binding.calendarView.setSelectedDate(date)
                setData(date!!.year, date!!.month)
            }
        })
    }
}