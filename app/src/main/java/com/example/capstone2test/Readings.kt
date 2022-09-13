package com.example.capstone2test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capstone2test.adapter.ReadingsPagerAdapter
import com.example.capstone2test.databinding.FragmentReadingBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class Readings : Fragment() {
    private var _binding: FragmentReadingBinding?=null
    private val binding get() = _binding!!

    //TabLayoutMediator
    var mediator: TabLayoutMediator? = null

    //Adapter
    var adapter: ReadingsPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentReadingBinding.inflate(inflater,container,false)
        //code
        //Initializing
        adapter = ReadingsPagerAdapter(parentFragmentManager, lifecycle)
        mediator = TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = adapter!!.getHeaders(
                position
            )
        }



        //setAdapter
        binding.viewPager.adapter = adapter

        //AttachMediator

        //AttachMediator
        if (!mediator!!.isAttached) mediator!!.attach()

        return binding.root
    }

}