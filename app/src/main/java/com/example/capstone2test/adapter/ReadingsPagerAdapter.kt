package com.example.capstone2test.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capstone2test.AddReadings
import com.example.capstone2test.ViewReadings
import java.util.ArrayList

class ReadingsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    // Fields:
    private val fragments: MutableList<Fragment> = ArrayList()
    private val headers: MutableList<String> = ArrayList()
    private fun initData() {
        addData(AddReadings(), "Add")
        addData(ViewReadings(), "View")
    }

    private fun addData(fragment: Fragment, header: String) {
        //add fragment
        fragments.add(fragment)

        //add header
        headers.add(header)
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    fun getHeaders(pos: Int): String {
        return headers[pos]
    }

    init {
        initData()
    }
}