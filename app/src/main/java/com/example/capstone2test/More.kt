package com.example.capstone2test

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentMoreBinding
import com.google.gson.Gson

class More : Fragment() {
    private var _binding:FragmentMoreBinding?=null
    private val binding get() = _binding!!
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.roatate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }
    private var clicked:Boolean =false
    private lateinit var action: NavDirections
    private lateinit var token: String
    private  lateinit var queue: VolleySingleton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMoreBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireActivity().getApplicationContext()).getToken().getToken();
        queue= VolleySingleton.getInstance(requireContext())
        binding.settingsbottomNavView.setOnItemReselectedListener {item ->
            when(item.itemId){
                R.id.homepage7 -> {
                    action = MoreDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings7 -> {
                    action = MoreDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }

        binding.settingsSignoutTv.setOnClickListener {

            SessionManager.getInstance(requireActivity().applicationContext).isUserLoggedIn = false
            requireActivity().finish()
           startActivity(Intent(requireContext(),MainActivity::class.java))

        }
        binding.settingsaddFoodNow.setOnClickListener {
            action = FoodJournalDirections.actionGlobalAddfood()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.settingsaddMedicationNow.setOnClickListener {
            action = FoodJournalDirections.actionGlobalAddmed()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.settingsbottomNavViewFAB.setOnClickListener {
            onNavFabClick()
        }

        return binding.root
    }
    private fun onNavFabClick(){
        setVisiblity(clicked)
        setAnimation(clicked)
        setClicable(clicked)
        clicked=!clicked

    }
    private fun setVisiblity(clicked:Boolean){
        if (!clicked){
            binding.settingsaddFoodNow.visibility=View.VISIBLE
            binding.settingsaddMedicationNow.visibility=View.VISIBLE
        }else {

            binding.settingsaddFoodNow.visibility=View.INVISIBLE
            binding.settingsaddMedicationNow.visibility=View.INVISIBLE
        }


    }
    private  fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.settingsaddFoodNow.startAnimation(fromBottom)
            binding.settingsaddMedicationNow.startAnimation(fromBottom)
            binding.settingsbottomNavViewFAB.startAnimation(rotateOpen)
        }else {
            binding.settingsaddFoodNow.startAnimation(toBottom)
            binding.settingsaddMedicationNow.startAnimation(toBottom)
            binding.settingsbottomNavViewFAB.startAnimation(rotateClose)

        }

    }
    private  fun setClicable(clicked: Boolean){
        if (!clicked){
            binding.settingsaddFoodNow.isClickable=true
            binding.settingsaddMedicationNow.isClickable=true
        }else {

            binding.settingsaddFoodNow.isClickable=false
            binding.settingsaddMedicationNow.isClickable=false
        }
    }




}