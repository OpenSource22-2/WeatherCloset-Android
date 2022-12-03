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
import com.example.opensource.R
import com.example.opensource.data.remote.RecordData
import com.example.opensource.databinding.FragmentRecordBinding
import com.example.opensource.databinding.ViewUserRecordBinding


class RecordFragment(private val recordData: RecordData) : DialogFragment() {

    private lateinit var binding: FragmentRecordBinding

    companion object {
        const val RECORD_DATA = "recordData"
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
        clickModifyBtn()
        return binding.root
    }

    private fun setData() {
        val layout = binding.layoutRecord
        layout.tvDate.text = recordData.recordDate
        Glide.with(this).load(recordData.imageUrl).into(layout.ivRecord)
        setIcon(layout, recordData.icon)
        layout.rbStar.rating = recordData.stars.toFloat()
//        setTag(layout, recordData.tag) // TODO: tag
        layout.tvMemo.text = recordData.comment
        layout.tvTemperature.text = recordData.temperature.toString()
        setHeart()
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