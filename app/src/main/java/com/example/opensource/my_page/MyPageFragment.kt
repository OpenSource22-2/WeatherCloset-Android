package com.example.opensource.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.opensource.R
import com.example.opensource.databinding.FragmentMyPageBinding
import com.google.android.material.tabs.TabLayoutMediator


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private lateinit var tabViewPagerAdapter: MyPageTabViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        initAdapter()
        initTabLayout()
        return binding.root
    }

    private fun initAdapter() {
        val fragmentList = listOf(UserRecordFragment(), MyPageLikeFragment())

        tabViewPagerAdapter = MyPageTabViewPagerAdapter(this)
        tabViewPagerAdapter.fragments.addAll(fragmentList)

        binding.vpHome.adapter = tabViewPagerAdapter
    }

    private fun initTabLayout() {
        val tabIcon = listOf(R.drawable.ic_user_record_select, R.drawable.ic_user_like_select)

        TabLayoutMediator(binding.tlUser, binding.vpHome) { tab, position ->
            tab.setIcon(tabIcon[position])
        }.attach()
    }
}