package com.example.opensource

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {

    private const val MEMBER_ID = "MEMBER_ID"

    fun setMemberId(context: Context, memberId: Int) {
        val pref: SharedPreferences =
            context.getSharedPreferences(MEMBER_ID, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(MEMBER_ID, memberId)
        editor.apply()
    }

    fun getMemberId(context: Context): Int {
        val pref: SharedPreferences = context.getSharedPreferences(MEMBER_ID, Context.MODE_PRIVATE)
        return pref.getInt(MEMBER_ID, Secret.memberId)
    }
}