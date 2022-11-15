package com.example.opensource.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.opensource.databinding.ActivitySetImageBinding

class SetImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySetImageBinding.inflate(layoutInflater)

        val imagePath = intent.getStringExtra("path")
        binding.tvImagePath.text = imagePath
        Glide.with(this)
            .load(imagePath)
            .into(binding.ivImage)

        setContentView(binding.root)
    }
}