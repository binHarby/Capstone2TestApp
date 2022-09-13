package com.example.capstone2test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.capstone2test.roomDatabase.ReadingViewModel
import com.example.capstone2test.adapter.ReadingsViewAdapter
import com.example.capstone2test.databinding.FragmentViewReadingsBinding

class ViewReadings : Fragment() {
    private var _binding: FragmentViewReadingsBinding? = null
    private val binding get() = _binding!!
    lateinit  var adapter: ReadingsViewAdapter
    private lateinit var readingViewModel: ReadingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewReadingsBinding.inflate(inflater, container, false)

        //Initializing

        readingViewModel = ViewModelProvider(this)[ReadingViewModel::class.java]
        readingViewModel.readAllData.observe(viewLifecycleOwner, Observer { reading ->
            adapter=ReadingsViewAdapter(requireContext(), reading )
            binding.rvReadings.adapter=adapter
        })


        return binding.root
    }

}