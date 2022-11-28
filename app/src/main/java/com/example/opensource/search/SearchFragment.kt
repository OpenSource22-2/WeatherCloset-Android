package com.example.opensource.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.opensource.MainActivity
import com.example.opensource.databinding.FragmentSearchBinding


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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNumberPicker()
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
            if(binding.tvMin.text.toString() > binding.tvMax.text.toString())
                Toast.makeText(requireContext(), "범위를 확인해주세요.", Toast.LENGTH_SHORT).show()
            else{
                // 데이터 불러오기

            }
        }
    }
}