package com.example.opensource.search

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.NumberPicker
import com.example.opensource.R

class NumberPickerDialog(context: Context) {

    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    companion object {
        const val MIN_VALUE_1: Int = -99
        const val MAX_VALUE_1: Int = 99
        const val MIN_VALUE_2: Int = 0
        const val MAX_VALUE_2: Int = 9
    }

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    interface OnDialogClickListener
    {
        fun onClicked(t1: Int, t2: Int)
    }

    fun showDialog(){

        dialog.setContentView(R.layout.dialog_number_picker)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                                    WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val btOk = dialog.findViewById<Button>(R.id.bt_ok)
        val btCancel = dialog.findViewById<Button>(R.id.bt_cancel)
        val np1 = dialog.findViewById<NumberPicker>(R.id.np_first)
        val np2 = dialog.findViewById<NumberPicker>(R.id.np_second)

        np1.minValue = 0
        np1.maxValue = MAX_VALUE_1 - MIN_VALUE_1
        np1.value = np1.value - MIN_VALUE_1
        np1.setFormatter(NumberPicker.Formatter {
            index -> Integer.toString(index + MIN_VALUE_1) })
/*                index -> if(index< MIN_VALUE_1)  Integer.toString(index)
                            else Integer.toString(index + 2 * MIN_VALUE_1 - 1) })*/

        np2.minValue = MIN_VALUE_2
        np2.maxValue = MAX_VALUE_2

        btOk.setOnClickListener{
            onClickListener.onClicked(np1.value, np2.value)
            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}