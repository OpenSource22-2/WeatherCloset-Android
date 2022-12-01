package com.example.opensource.record

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.opensource.R
import com.example.opensource.data.remote.RecordData
import com.example.opensource.databinding.FragmentRecordBinding
import com.example.opensource.databinding.ViewUserRecordBinding


class RecordFragment : DialogFragment() {

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
        val recordData = RecordData(
            id = 1,
            username = "최유빈",
            imageUrl = "https://avatars.githubusercontent.com/u/48249505?v=4",
            stars = 5,
            comment = "좋아요",
            heart = true,
            recordDate = "2021. 08. 01",
            temperature = 25.0,
            icon = 1,
            tag = listOf("쌀쌀해요", "추워요"),
        ) // dummy
        val layout = binding.layoutRecord
        layout.tvDate.text = recordData.recordDate
        Glide.with(this).load(recordData.imageUrl).into(layout.ivRecord)
        setIcon(layout, recordData.icon)
        layout.rbStar.rating = recordData.stars.toFloat()
        setTag(layout, recordData.tag)
        layout.tvMemo.text = recordData.comment
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
            // start recordModifyActivity
            val intent = Intent(requireContext(), RecordModifyActivity::class.java)
            val recordData = RecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://avatars.githubusercontent.com/u/48249505?v=4",
                stars = 5,
                comment = "좋아요",
                heart = true,
                recordDate = "2021. 08. 01",
                temperature = 25.0,
                icon = 1,
                tag = listOf("쌀쌀해요", "추워요"),
            ) // dummy
            intent.putExtra(RECORD_DATA, recordData)
            startActivity(intent)
            dismiss()
        }
    }
}