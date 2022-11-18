package com.example.opensource.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.opensource.R
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.databinding.ItemHomeBinding

class HomeRecordRvAdapter(private val context: Context) :
    RecyclerView.Adapter<HomeRecordRvAdapter.HomeRecordViewHolder>() {

    var recordList = mutableListOf<HomeRecordResponse>()
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun addItems(data: ArrayList<HomeRecordResponse>){
        this.recordList.addAll(data)
        notifyDataSetChanged()  //변경된 data 적용
    }

    inner class HomeRecordViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: HomeRecordResponse) {
            Glide.with(context)
                .load(data.imageUrl)
                .into(binding.ivClothes)
            binding.tvDate.text = data.createdAt
            if (data.heart)
                binding.ivHeart.setImageResource(R.drawable.heart_full)
            else
                binding.ivHeart.setImageResource(R.drawable.heart_empty)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecordViewHolder {
        val binding = ItemHomeBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return HomeRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeRecordViewHolder, position: Int) {
        holder.onBind(recordList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int = recordList.size

}