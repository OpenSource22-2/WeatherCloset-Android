package com.example.opensource.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opensource.data.remote.MyPageLikeResponse
import com.example.opensource.databinding.FragmentMyPageLikeBinding


class MyPageLikeFragment : Fragment() {
    private lateinit var binding: FragmentMyPageLikeBinding
    private lateinit var recordRvAdapter: MyPageLikeRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyPageLikeBinding.inflate(inflater, container, false)

        getRecordList() // dummy
        clickItemView()
        return binding.root
    }

    private fun initAdapter(data: List<MyPageLikeResponse.MyPageLikeData>) {

    }

    private fun clickItemView() {
        recordRvAdapter.setItemClickListener(object : MyPageLikeRvAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // TODO: 다이얼로그
                Toast.makeText(requireContext(), "ITEM CLICK", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getRecordList() {
        val data = ArrayList<MyPageLikeResponse.MyPageLikeData>()
        data.add(
            MyPageLikeResponse.MyPageLikeData(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                heart = true,
                createdAt = "2022-11-24",
                id = 1
            )
        )
        data.add(
            MyPageLikeResponse.MyPageLikeData(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                heart = true,
                createdAt = "2022-11-24",
                id = 1
            )
        )
        data.add(
            MyPageLikeResponse.MyPageLikeData(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                heart = true,
                createdAt = "2022-11-24",
                id = 1
            )
        )
        data.add(
            MyPageLikeResponse.MyPageLikeData(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                heart = true,
                createdAt = "2022-11-24",
                id = 1
            )
        )
        data.add(
            MyPageLikeResponse.MyPageLikeData(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                heart = true,
                createdAt = "2022-11-24",
                id = 1
            )
        )
        data.add(
            MyPageLikeResponse.MyPageLikeData(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                heart = true,
                createdAt = "2022-11-24",
                id = 1
            )
        )
        data.add(
            MyPageLikeResponse.MyPageLikeData(
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                heart = true,
                createdAt = "2022-11-24",
                id = 1
            )
        )
        recordRvAdapter = MyPageLikeRvAdapter(requireContext())
        recordRvAdapter.addItems(data)
        recordRvAdapter.notifyDataSetChanged()
        binding.rvLike.adapter = recordRvAdapter
    }
}