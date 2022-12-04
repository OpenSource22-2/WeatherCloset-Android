package com.example.opensource.record

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.opensource.MySharedPreference
import com.example.opensource.R
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.BaseResponse
import com.example.opensource.data.remote.RecordData
import com.example.opensource.databinding.FragmentRecordBinding
import com.example.opensource.databinding.ViewUserRecordBinding
import com.example.opensource.my_page.MyPageLikeFragment
import com.example.opensource.my_page.MyPageRecordFragment
import com.example.opensource.util.setOnSinglePostClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class RecordFragment(private val recordData: RecordData) : DialogFragment() {

    private lateinit var binding: FragmentRecordBinding

    private lateinit var state: String

    companion object {
        const val RECORD_DATA = "RECORD_DATA"
        const val TAG = "RECORD_FRAGMENT"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.DisableDialogBackground)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordBinding.inflate(inflater, container, false)

        setData()
        getState()
        clickModifyBtn()
        return binding.root
    }

    private fun getState() {
        state = this.tag!!
        if (state == MyPageLikeFragment.TAG) {
            binding.tvModify.visibility = View.GONE
            setUserName()
            clickIvHeart()
        }
    }

    private fun setUserName() {
        val date = recordData.recordDate.replaceFirst(".", "년")
            .replaceFirst(".", "월")
            .plus("일")
        val userNameDate = recordData.username + "님의 " + date
        binding.layoutRecord.tvUserNameDate.text = userNameDate
        binding.layoutRecord.tvDate.visibility = View.INVISIBLE
    }

    private fun modifyHeart(heart: Boolean) {
        val call = RetrofitObject.provideWeatherClosetApi.likeRecord(
            memberId = MySharedPreference.getMemberId(requireContext()),
            recordId = recordData.id,
            body = heart
        )

        call.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                    // notifyDataSetChanged
                    parentFragment?.onStart()
                } else {
                    Log.e(TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }
        })
    }

    private fun clickIvHeart() {
        binding.layoutRecord.ivHeart.setOnSinglePostClickListener {
            binding.layoutRecord.ivHeart.isSelected = !binding.layoutRecord.ivHeart.isSelected
            if (binding.layoutRecord.ivHeart.isSelected) {
                binding.layoutRecord.ivHeart.setImageResource(R.drawable.heart_white_line)
            } else {
                binding.layoutRecord.ivHeart.setImageResource(R.drawable.heart_empty)
            }
            modifyHeart(binding.layoutRecord.ivHeart.isSelected)
        }
    }

    private fun setData() {
        val layout = binding.layoutRecord
        layout.tvDate.text = recordData.recordDate
        if (recordData.icon == -1) {
            setTodayWeather()
        }
        Glide.with(this).load(recordData.imageUrl).into(layout.ivRecord)
        setIcon(layout, recordData.icon)
        layout.rbStar.rating = recordData.stars.toFloat()
        setTag(layout, recordData.tags)
        layout.tvMemo.text = recordData.comment
        layout.tvTemperature.text = recordData.temperature.toString()
        layout.ivHeart.isSelected = recordData.heart
        setHeart()
    }

    private fun setTodayWeather() {
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

    private fun setHeart() {
        if (recordData.heart) {
            binding.layoutRecord.ivHeart.setImageResource(R.drawable.heart_white_line)
        } else {
            binding.layoutRecord.ivHeart.setImageResource(R.drawable.heart_empty)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutSize(view)
    }

    private fun setLayoutSize(view: View) {
        view.layoutParams.width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        view.layoutParams.height = (resources.displayMetrics.heightPixels * 0.70).toInt()
    }

    private fun clickModifyBtn() {
        binding.tvModify.setOnClickListener {
            val intent = Intent(requireContext(), RecordModifyActivity::class.java)
            Log.d("TAG", "clickModifyBtn: $recordData")
            intent.putExtra(RECORD_DATA, recordData)
            startActivity(intent)
            dismiss()
        }
    }
}