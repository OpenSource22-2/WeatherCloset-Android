package com.example.opensource.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.opensource.MainActivity
import com.example.opensource.data.remote.HomeRecordResponse
import com.example.opensource.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var recordRvAdapter: SearchRecordRvAdapter
    private var data = arrayListOf<HomeRecordResponse.HomeRecordData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        setNumberPicker()
        getRecordList()
        return binding.root
    }

    private fun initAdapter(data: List<HomeRecordResponse.HomeRecordData>) {
        recordRvAdapter = SearchRecordRvAdapter(requireContext())
        recordRvAdapter.addItems(data)
        recordRvAdapter.notifyDataSetChanged()
        binding.rvRecord.adapter = recordRvAdapter
    }

    private fun getRecordList() {

        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150512_.png?alt=media&token=df1d84f8-c328-4464-a84e-1ce3e08ed44c",
                stars = 5,
                comment = "comment",
                createdAt = "2022-11-20",
                heart = false,
                temperature = 21
            )
        )
        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150343_.png?alt=media&token=87d29947-38ea-4344-afda-dbd59c98ed1d",
                stars = 5,
                comment = "comment",
                createdAt = "2022-11-18",
                heart = true,
                temperature = 10
            )
        )
        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150442_.png?alt=media&token=8ad275e0-8258-4e11-bad2-23b6ddd0c219",
                stars = 5,
                comment = "comment",
                createdAt = "2022-11-19",
                heart = true,
                temperature = 15
            )
        )
        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150442_.png?alt=media&token=8ad275e0-8258-4e11-bad2-23b6ddd0c219",
                stars = 5,
                comment = "comment",
                createdAt = "2022-12-18",
                heart = true,
                temperature = 14
            )
        )
        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150442_.png?alt=media&token=8ad275e0-8258-4e11-bad2-23b6ddd0c219",
                stars = 5,
                comment = "comment",
                createdAt = "2022-11-01",
                heart = true,
                temperature = 29
            )
        )
        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150442_.png?alt=media&token=8ad275e0-8258-4e11-bad2-23b6ddd0c219",
                stars = 5,
                comment = "comment",
                createdAt = "2022-12-29",
                heart = true,
                temperature = 10
            )
        )
        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150442_.png?alt=media&token=8ad275e0-8258-4e11-bad2-23b6ddd0c219",
                stars = 5,
                comment = "comment",
                createdAt = "2022-11-01",
                heart = false,
                temperature = 11
            )
        )
        data.add(
            HomeRecordResponse.HomeRecordData(
                id = 1,
                username = "최유빈",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/weathercloset-78954.appspot.com/o/item%2FIMAGE_20221118_150442_.png?alt=media&token=8ad275e0-8258-4e11-bad2-23b6ddd0c219",
                stars = 5,
                comment = "comment",
                createdAt = "2022-11-01",
                heart = true,
                temperature = 12
            )
        )
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
                // 범위 설정
                val selectedData = data
                selectedData.removeIf{n: HomeRecordResponse.HomeRecordData
                                        -> (n.temperature < minT || n.temperature > maxT)}
                // 최신순으로 정렬
                selectedData.sortWith(compareByDescending<HomeRecordResponse.HomeRecordData> {it.createdAt}.thenBy {!it.heart})
                // 데이터 불러오기
                initAdapter(selectedData)
            }
        }
    }
}