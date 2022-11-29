package com.example.opensource.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.opensource.R
import com.example.opensource.databinding.FragmentRecordModifyBinding


class RecordModifyFragment : DialogFragment() {

    private lateinit var binding: FragmentRecordModifyBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.DisableDialogBackground)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordModifyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayoutSize(view)
    }

    private fun setLayoutSize(view: View) {
        view.layoutParams.width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        view.layoutParams.height = (resources.displayMetrics.heightPixels * 0.70).toInt()
    }
}