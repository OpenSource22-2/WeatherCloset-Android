package com.example.opensource.my_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opensource.MySharedPreference
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
            RetrofitObject.provideWeatherClosetApi.getRecordList(   // TODO: 좋아요 api로 변경
                MySharedPreference.getMemberId(
                    requireContext()
                )
            )

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
}