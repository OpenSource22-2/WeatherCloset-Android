package com.example.opensource.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.opensource.MySharedPreference
import com.example.opensource.R
import com.example.opensource.Secret
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.CalendarRecordResponse
import com.example.opensource.data.remote.RecordData
import com.example.opensource.data.remote.RecordResponse
import com.example.opensource.data.remote.WeatherData
import com.example.opensource.databinding.FragmentCalendarBinding
import com.example.opensource.databinding.ViewUserRecordBinding
import com.example.opensource.home.HomeFragment
import com.example.opensource.record.RecordFragment
import com.prolificinteractive.materialcalendarview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class CalendarFragment : Fragment() {

    private lateinit var userDailyRecord: View
    private lateinit var binding: FragmentCalendarBinding
    // 유저 데이터 받아오기
    private val recordSet = mutableSetOf<CalendarRecordResponse.CalendarRecordData>()
    private val dateSet = mutableSetOf<CalendarDay>()

    override fun onStart() {
        super.onStart()
        // Log.d(HomeFragment.TAG, "onStart: ")
        initCalendarView()
        setCalendarView()
    }

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
    }

    private fun setData(widget: MaterialCalendarView, date: CalendarDay){
        val mdate = String.format("%d. %02d. %02d", date.year, date.month, date.day)
        val record = recordSet.find { it.recordDate.contains(mdate) }
        Log.d("mdate", mdate + recordSet.size)

        // 기록이 존재하는 경우 화면에 표시
        if(record!=null){
            widget.state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit()

            getDailyRecord(record.id)
            userDailyRecord.setVisibility(View.VISIBLE)
            binding.clPlz.visibility = View.INVISIBLE
        }
        else{
            userDailyRecord.setVisibility(View.INVISIBLE)
            binding.clPlz.visibility = View.VISIBLE
        }
    }

    fun getDailyRecord(recordId: Int) {
        val call: Call<RecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getRecord(recordId)

        call.enqueue(object : Callback<RecordResponse> {
            override fun onResponse(
                call: Call<RecordResponse>,
                response: Response<RecordResponse>
            ) {
                if (response.isSuccessful) {
                    //Log.d(HomeFragment.TAG, "onResponse: ${response.body()}")
                    val recordData = response.body()?.data!!
                    Log.d("recordData", "recordData" + recordData.recordDate)
                    setDailyRecord(recordData)
                } else {
                    //Log.e(HomeFragment.TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<RecordResponse>, t: Throwable) {
                //Log.d(HomeFragment.TAG, "onFailure: $t")
            }
        })
    }

    private fun setDailyRecord(recordData: RecordData){
        binding.userDailyRecord.tvDate.text = recordData.recordDate
        if(recordData.icon == -1){
            setTodayWeather(recordData)
        }
        Glide.with(requireContext())
            .load(recordData.imageUrl)
            .fitCenter()
            .into(binding.userDailyRecord.ivRecord)
        setIcon(binding.userDailyRecord, recordData.icon)
        binding.userDailyRecord.tvTemperature.text = recordData.temperature.toString() + "˚"
        binding.userDailyRecord.rbStar.rating = recordData.stars.toFloat()
        setTag(binding.userDailyRecord, recordData.tags)
        binding.userDailyRecord.tvMemo.text = recordData.comment
        setHeart(recordData)
    }

    private fun setTodayWeather(recordData: RecordData) {
        val todayDate = SimpleDateFormat("yyyy. MM. dd").format(System.currentTimeMillis())
        if (recordData.recordDate == todayDate) {
            recordData.icon = MySharedPreference.getIcon(requireContext())
            recordData.temperature = MySharedPreference.getTemperature(requireContext()).toDouble()
        }
    }

    private fun setIcon(layout: ViewUserRecordBinding, icon: Int) {
        when (icon) {
            1 -> layout.ivTemperature.setImageResource(R.drawable.ic_13n)
            2 -> layout.ivTemperature.setImageResource(R.drawable.ic_10d)
            3 -> layout.ivTemperature.setImageResource(R.drawable.ic_04d)
            4 -> layout.ivTemperature.setImageResource(R.drawable.ic_03d)
            5 -> layout.ivTemperature.setImageResource(R.drawable.ic_01d)
        }
    }

    private fun setTag(layout: ViewUserRecordBinding, tagList: List<String>) {
        if (tagList.isNotEmpty()) {
            layout.chip1.text = tagList[0]
            layout.chip1.visibility = View.VISIBLE
        }
        if (tagList.size >= 2) {
            layout.chip2.text = tagList[1]
            layout.chip2.visibility = View.VISIBLE
        }
        if (tagList.size >= 3) {
            layout.chip3.text = tagList[2]
            layout.chip3.visibility = View.VISIBLE
        }
    }

    private fun setHeart(recordData: RecordData) {
        if (recordData.heart) {
            binding.userDailyRecord.ivHeart.setImageResource(R.drawable.heart_white_line)
        } else {
            binding.userDailyRecord.ivHeart.setImageResource(R.drawable.heart_empty)
        }
    }

    private fun getMonthRecord(year: Int, month: Int){
        val call: Call<CalendarRecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getCalendarRecord(
                MySharedPreference.getMemberId(
                requireContext()
            ), year, month)

        call.enqueue(object : Callback<CalendarRecordResponse> {
            override fun onResponse(
                call: Call<CalendarRecordResponse>,
                response: Response<CalendarRecordResponse>
            ) {
                if (response.isSuccessful) {
                    // 레코드 받아오기
                    recordSet.addAll(response.body()!!.data)
                    // 레코드에서 날짜만 가공하여 추출
                    getDate()
                    // 데이터가 존재하는 경우 달력에 표시
                    binding.calendarView.addDecorators(EventDecorator(dateSet, activity))
                } else {
                    // Log.e(CalendarFragment.TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<CalendarRecordResponse>, t: Throwable) {
                // Log.d(CalendarFragment.TAG, "onFailure: $t")
            }
        })
    }

    private fun getDate(){
        for(date in recordSet){
            val mDate = date.recordDate.replace(" ", "")
            val toCalendarDay = mDate.split(".")
            dateSet.add(CalendarDay.from(toCalendarDay[0].toInt(), toCalendarDay[1].toInt(), toCalendarDay[2].toInt()))
        }
    }

    private fun initCalendarView(){
        getMonthRecord(binding.calendarView.currentDate.year, binding.calendarView.currentDate.month)
        binding.calendarView.clearSelection()
        /*binding.calendarView.setSelectedDate(binding.calendarView.currentDate)
        setData(binding.calendarView, binding.calendarView.selectedDate!!)*/
    }

    private fun setCalendarView(){
        // 먼슬리, 위클리 변환
        binding.icChange.setOnClickListener {
            if(binding.calendarView.calendarMode==CalendarMode.MONTHS){
                binding.calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit()
                if(dateSet.contains(binding.calendarView.selectedDate))
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
                setData(widget, date)
            }
        })

        // 달을 바꾸는 경우
        binding.calendarView.setOnMonthChangedListener(object: OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
                getMonthRecord(date!!.year, date!!.month)
            }
        })
    }
}