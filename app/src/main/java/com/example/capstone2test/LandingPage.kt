package com.example.capstone2test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.databinding.FragmentLandingPageBinding


class LandingPage : Fragment() {
    private var _binding: FragmentLandingPageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentLandingPageBinding.inflate(inflater,container,false)
       //code here
        binding.landingBtn1.setOnClickListener {
            val action = LandingPageDirections.landingToSignup()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.landingBtn2.setOnClickListener {
            val action = LandingPageDirections.landingToSignin()
            Navigation.findNavController(binding.root).navigate(action)
        }
        return  binding.root
    }

    override fun onStart() {
        super.onStart()
        if(      SessionManager.getInstance(requireActivity().applicationContext).isUserLoggedIn)
        {
            val action = LandingPageDirections.actionLandingPageToHomepage()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

}