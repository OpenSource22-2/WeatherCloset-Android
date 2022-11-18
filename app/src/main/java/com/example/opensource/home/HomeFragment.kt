package com.example.opensource.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recordRvAdapter: HomeRecordRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        recordRvAdapter = HomeRecordRvAdapter(requireContext())
        recordRvAdapter.addItems(createDummy()) // dummy
        binding.rvRecord.adapter = recordRvAdapter

        clickRecordItemView()
        return binding.root
    }

    private fun createDummy(): ArrayList<HomeRecordResponse> {
        val data = ArrayList<HomeRecordResponse>()

        data.add(HomeRecordResponse("https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c", 5, "comment", "2022-11-18", false, 11))
        data.add(HomeRecordResponse("https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150343_.png?alt=media&token=87d29947-38ea-4344-afda-dbd59c98ed1d", 5, "comment", "2022-11-18", true, 11))
        data.add(HomeRecordResponse("https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150442_.png?alt=media&token=8ad275e0-8258-4e11-bad2-23b6ddd0c219", 5, "comment", "2022-11-18", true, 11))
        data.add(HomeRecordResponse("https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c", 5, "comment", "2022-11-18", true, 11))

        return data
    }

    private fun clickRecordItemView() {
        recordRvAdapter.setItemClickListener(object :
            HomeRecordRvAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // TODO: 다이얼로
                Toast.makeText(requireContext(), "ITEM CLICK", Toast.LENGTH_SHORT).show()
            }
        })
    }
}