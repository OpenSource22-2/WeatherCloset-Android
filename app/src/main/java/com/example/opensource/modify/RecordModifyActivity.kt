package com.example.opensource.modify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.opensource.R
import com.example.opensource.databinding.ActivityRecordModifyBinding

class RecordModifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContent()
    }

    private fun setContent() {
//        binding.tvDate.text = intent.getStringExtra("date")
//        binding.tvClothes.text = intent.getStringExtra("clothes")
//        binding.tvWeather.text = intent.getStringExtra("weather")
//        binding.tvTemperature.text = intent.getStringExtra("temperature")
//        binding.tvHumidity.text = intent.getStringExtra("humidity")
//        binding.tvMemo.text = intent.getStringExtra("memo")
    }

}