package com.example.opensource.my_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opensource.Secret
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.data.remote.RecordResponse
import com.example.opensource.databinding.FragmentMyPageLikeBinding
import com.example.opensource.home.HomeFragment
import com.example.opensource.home.HomeRecordRvAdapter
import com.example.opensource.record.RecordFragment
import com.example.opensource.util.GridSpacingItemDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPageLikeFragment : Fragment() {
    private lateinit var binding: FragmentMyPageLikeBinding
    private lateinit var recordRvAdapter: HomeRecordRvAdapter

    companion object {
        const val TAG = "MY_PAGE_LIKE_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyPageLikeBinding.inflate(inflater, container, false)

//        getRecordList() // dummy
//        clickItemView()
        binding.rvLike.addItemDecoration(GridSpacingItemDecoration(2, 15, true))
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        setData()
    }

    private fun setData() {
        val call: Call<HomeRecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getRecordList(Secret.memberId)

        call.enqueue(object : Callback<HomeRecordResponse> {
            override fun onResponse(
                call: Call<HomeRecordResponse>,
                response: Response<HomeRecordResponse>
            ) {
                if (response.isSuccessful) {
                    initAdapter(response.body()?.data!!)
                } else {
                    Log.e(HomeFragment.TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<HomeRecordResponse>, t: Throwable) {
                Log.d(HomeFragment.TAG, "onFailure: $t")
            }
        })
    }

    private fun initAdapter(data: List<HomeRecordResponse.HomeRecordData>) {
        recordRvAdapter = HomeRecordRvAdapter(requireContext())
        recordRvAdapter.addItems(data)
        recordRvAdapter.notifyDataSetChanged()
        binding.rvLike.adapter = recordRvAdapter
        // itemDecoration
        clickRecordItemView()
    }

    private fun clickRecordItemView() {
        recordRvAdapter.setItemClickListener(object :
            HomeRecordRvAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                // 기록 단건 조회
                val recordId = recordRvAdapter.recordList[position].id
                getRecordInfo(recordId)
            }
        })
    }

    fun getRecordInfo(recordId: Int) {
        val call: Call<RecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getRecord(recordId)

        call.enqueue(object : Callback<RecordResponse> {
            override fun onResponse(
                call: Call<RecordResponse>,
                response: Response<RecordResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(HomeFragment.TAG, "onResponse: ${response.body()}")
                    val recordData = response.body()?.data!!
                    // show dialog
                    val dialog = RecordFragment(recordData)
                    dialog.show(childFragmentManager, TAG)
                } else {
                    Log.e(HomeFragment.TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<RecordResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }
        })
    }

//    private fun getRecordList() {
//        val data = ArrayList<MyPageLikeResponse.MyPageLikeData>()
//        data.add(
//            MyPageLikeResponse.MyPageLikeData(
//                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
//                heart = true,
//                createdAt = "2022-11-24",
//                id = 1
//            )
//        )
//        data.add(
//            MyPageLikeResponse.MyPageLikeData(
//                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
//                heart = true,
//                createdAt = "2022-11-24",
//                id = 1
//            )
//        )
//        data.add(
//            MyPageLikeResponse.MyPageLikeData(
//                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
//                heart = true,
//                createdAt = "2022-11-24",
//                id = 1
//            )
//        )
//        data.add(
//            MyPageLikeResponse.MyPageLikeData(
//                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
//                heart = true,
//                createdAt = "2022-11-24",
//                id = 1
//            )
//        )
//        data.add(
//            MyPageLikeResponse.MyPageLikeData(
//                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
//                heart = true,
//                createdAt = "2022-11-24",
//                id = 1
//            )
//        )
//        data.add(
//            MyPageLikeResponse.MyPageLikeData(
//                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
//                heart = true,
//                createdAt = "2022-11-24",
//                id = 1
//            )
//        )
//        data.add(
//            MyPageLikeResponse.MyPageLikeData(
//                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
//                heart = true,
//                createdAt = "2022-11-24",
//                id = 1
//            )
//        )
//        recordRvAdapter = MyPageLikeRvAdapter(requireContext())
//        recordRvAdapter.addItems(data)
//        recordRvAdapter.notifyDataSetChanged()
//        binding.rvLike.adapter = recordRvAdapter
//    }
}