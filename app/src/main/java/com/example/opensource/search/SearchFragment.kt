package com.example.opensource.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import com.example.opensource.MainActivity
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.RecordResponse
import com.example.opensource.data.remote.SearchRecordResponse
import com.example.opensource.databinding.FragmentSearchBinding
import com.example.opensource.home.HomeFragment
import com.example.opensource.home.HomeRecordRvAdapter
import com.example.opensource.my_page.MyPageLikeFragment
import com.example.opensource.record.RecordFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var recordRvAdapter: SearchRecordRvAdapter

    companion object {
        const val TAG = "SEARCH_FRAGMENT"
//        const val MIN = 0.0
//        const val MAX = 99.9
    }

    override fun onStart() {
        super.onStart()
        Log.d(MyPageLikeFragment.TAG, "onStart: ")
        setData(binding.tvMin.text.toString().toDouble()
                , binding.tvMax.text.toString().toDouble())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        setNumberPicker()
        return binding.root
    }

    private fun setData(minTemperature: Double, maxTemperature: Double){
        val call: Call<SearchRecordResponse> =
            RetrofitObject.provideWeatherClosetApi.getSearchRecord(minTemperature, maxTemperature)

        call.enqueue(object : Callback<SearchRecordResponse> {
            override fun onResponse(
                call: Call<SearchRecordResponse>,
                response: Response<SearchRecordResponse>
            ) {
                if (response.isSuccessful) {
                    val sortedData = response.body()?.data!!
                    sortedData.sortWith(compareByDescending<SearchRecordResponse.SearchRecordData> { it.recordData }.thenBy { !it.heart })
                    initAdapter(sortedData)

                    if(sortedData.size==0){
                        binding.clBack.visibility = View.VISIBLE
                    }
                    else {
                        binding.clBack.visibility = View.INVISIBLE
                    }
                } else {
                    //Log.e(SearchFragment.TAG, "onResponse: response error: $response")
                }
            }

            override fun onFailure(call: Call<SearchRecordResponse>, t: Throwable) {
                //Log.d(SearchFragment.TAG, "onFailure: $t")
            }
        })
    }

    private fun initAdapter(data: List<SearchRecordResponse.SearchRecordData>) {
        recordRvAdapter = SearchRecordRvAdapter(requireContext())
        recordRvAdapter.addItems(data)
        recordRvAdapter.notifyDataSetChanged()
        binding.rvRecord.adapter = recordRvAdapter
        clickRecordItemView()
    }

    private fun clickRecordItemView() {
        recordRvAdapter.setItemClickListener(object :
            SearchRecordRvAdapter.OnItemClickListener {
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

    private fun setNumberPicker(){

        var activity =  getActivity() as MainActivity

        binding.tvMin.setOnClickListener{
            val dialog = NumberPickerDialog(activity)
            dialog.showDialog()
            dialog.setOnClickListener(object: NumberPickerDialog.OnDialogClickListener{
                override fun onClicked(t1: Int, t2: Int) {
                    val t3 = t1 - 99
                    binding.tvMin.text = t3.toString() + "." + t2.toString()
                }
            })
        }

        binding.tvMax.setOnClickListener{
            val dialog = NumberPickerDialog(activity)
            dialog.showDialog()
            dialog.setOnClickListener(object: NumberPickerDialog.OnDialogClickListener{
                override fun onClicked(t1: Int, t2: Int) {
                    val t3 = t1 - 99
                    binding.tvMax.text = t3.toString() + "." + t2.toString()
                }
            })
        }

        binding.icSearch.setOnClickListener{

            val minT = binding.tvMin.text.toString().toDouble()
            val maxT = binding.tvMax.text.toString().toDouble()

            if(minT > maxT)
                Toast.makeText(requireContext(), "범위를 확인해주세요.", Toast.LENGTH_SHORT).show()
            else{
                setData(minT, maxT)
            }
        }
    }
}