package com.example.capstone2test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.capstone2test.databinding.FragmentRecoverBinding
import java.util.*
import kotlin.concurrent.schedule

class Recover : Fragment() {
    private var _binding: FragmentRecoverBinding?=null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentRecoverBinding.inflate(inflater,container,false)
        //code
        binding.recoverBtn.setOnClickListener {
            binding.submitedMsg.visibility=View.VISIBLE
            Timer().schedule(2000) {
                val action = RecoverDirections.recoverToLandingPage()
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
        return binding.root
    }

}