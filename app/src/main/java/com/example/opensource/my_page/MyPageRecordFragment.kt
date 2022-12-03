package com.example.opensource.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.opensource.R

class MyPageRecordFragment : Fragment() {

    companion object {
        const val TAG = "USER_RECORD_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_page_record, container, false)
    }
}