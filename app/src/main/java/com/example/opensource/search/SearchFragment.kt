package com.example.opensource.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.opensource.MainActivity
import com.example.opensource.data.RetrofitObject
import com.example.opensource.data.remote.SearchRecordResponse
import com.example.opensource.databinding.FragmentSearchBinding
import com.example.opensource.home.HomeFragment.Companion.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var recordRvAdapter: SearchRecordRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        setNumberPicker()
        //getRecordList()
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
                    initAdapter(response.body()?.data!!)
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

        if(recordRvAdapter.recordList.isEmpty())
            binding.clBack.visibility = View.VISIBLE
        else
            binding.clBack.visibility = View.INVISIBLE
    }

    private fun setNumberPicker(){

        binding.clBack.visibility = View.VISIBLE

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
/*                // 범위 설정
                val selectedData = data
                selectedData.removeIf{n: SearchRecordResponse.SearchRecordData
                                        -> (n.temperature < minT || n.temperature > maxT)}
                // 최신순으로 정렬
                selectedData.sortWith(compareByDescending<SearchRecordResponse.SearchRecordData> {it.recordData}.thenBy {!it.heart})
                // 데이터 불러오기
                initAdapter(selectedData)*/
            }
        }
    }
}