package com.example.opensource.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.opensource.R
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.databinding.ItemSearchBinding

class SearchRecordRvAdapter(private val context: Context):
    RecyclerView.Adapter<SearchRecordRvAdapter.SearchRecordViewHolder>() {

    var recordList = mutableListOf<HomeRecordResponse.HomeRecordData>()

    fun addItems(data: List<HomeRecordResponse.HomeRecordData>) {
        this.recordList.addAll(data)
        notifyDataSetChanged()  //변경된 data 적용
    }

    inner class SearchRecordViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeRecordResponse.HomeRecordData) {
            Glide.with(context)
                .load(data.imageUrl)
                .into(binding.ivClothes)
            binding.tvDate.text = data.createdAt
            binding.tvTemperature.text = data.temperature.toString() + "˚"
            if (data.heart)
                binding.ivHeart.setImageResource(R.drawable.heart_full)
            else
                binding.ivHeart.setImageResource(R.drawable.heart_empty)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecordRvAdapter.SearchRecordViewHolder {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return SearchRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRecordRvAdapter.SearchRecordViewHolder, position: Int) {
        holder.onBind(recordList[position])
    }

    override fun getItemCount(): Int = recordList.size
}