package com.example.opensource.my_page

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.opensource.R
import com.example.opensource.data.remote.MyPageLikeResponse
import com.example.opensource.databinding.ItemMyPageBinding

class MyPageLikeRvAdapter(private val context: Context) :
    RecyclerView.Adapter<MyPageLikeRvAdapter.MyPageLikeViewHolder>() {

    var recordList = mutableListOf<MyPageLikeResponse.MyPageLikeData>()
    private lateinit var itemClickListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun addItems(data: List<MyPageLikeResponse.MyPageLikeData>) {
        this.recordList.addAll(data)
        notifyDataSetChanged()
    }

    inner class MyPageLikeViewHolder(private val binding: ItemMyPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: MyPageLikeResponse.MyPageLikeData) {
            Glide.with(context)
                .load(data.imageUrl)
                .into(binding.ivClothes)
            binding.tvDate.text = data.createdAt
            if (data.heart)
                binding.ivHeart.setImageResource(R.drawable.heart_white_line)
            else
                binding.ivHeart.setImageResource(R.drawable.heart_empty)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageLikeViewHolder {
        val binding = ItemMyPageBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyPageLikeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageLikeViewHolder, position: Int) {
        holder.onBind(recordList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int = recordList.size
}