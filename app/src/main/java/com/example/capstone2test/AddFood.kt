package com.example.capstone2test

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentAddFoodBinding
import com.example.capstone2test.model.User

class AddFood : Fragment() {
    private var _binding:FragmentAddFoodBinding?=null
    private val binding get()= _binding!!
    private val CAMERA_PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private lateinit var action:NavDirections
    private  lateinit var queue: VolleySingleton
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.roatate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }
    private var clicked:Boolean =false
    private var photo:Bitmap?=null
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding=FragmentAddFoodBinding.inflate(inflater,container,false)
        queue= VolleySingleton.getInstance(requireContext())



        user = SessionManager.getInstance(requireActivity().applicationContext).getUser()
        binding.addfoodCodeIcon.setOnClickListener {
            action=AddFoodDirections.actionAddFoodToAddBarcode()
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.addfoodCameraIcon.setOnClickListener {
            // Request permission
            /*
            val permissionGranted = requestCameraPermission()
            if (permissionGranted) {
                // Open the camera interface
                openCameraInterface()

            }

             */
            action=AddFoodDirections.actionAddFoodToAddImage()
            Navigation.findNavController(binding.root).navigate(action)
        }

        binding.addfoodbottomNavView.setOnItemReselectedListener {item ->
            when(item.itemId){
                R.id.homepage -> {
                    action=AddFoodDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings0 -> {
                    action = AddFoodDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }
        binding.addfoodbottomNavViewFAB.setOnClickListener {
            onNavFabClick()
        }
        binding.fromaddfoodaddFoodNow.setOnClickListener {
            action = AddFoodDirections.actionGlobalAddfood()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.fromaddfoodaddMedicationNow.setOnClickListener {
            action = AddFoodDirections.actionGlobalAddmed()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.addfoodSearchView.setOnClickListener {
            action = AddFoodDirections.actionAddFoodToAddText()
            Navigation.findNavController(binding.root).navigate(action)
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
            binding.fromaddfoodaddFoodNow.visibility=View.VISIBLE
            binding.fromaddfoodaddMedicationNow.visibility=View.VISIBLE
        }else {

            binding.fromaddfoodaddFoodNow.visibility=View.INVISIBLE
            binding.fromaddfoodaddMedicationNow.visibility=View.INVISIBLE
        }


    }
    private  fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.fromaddfoodaddFoodNow.startAnimation(fromBottom)
            binding.fromaddfoodaddMedicationNow.startAnimation(fromBottom)
            binding.addfoodbottomNavViewFAB.startAnimation(rotateOpen)
        }else {
            binding.fromaddfoodaddFoodNow.startAnimation(toBottom)
            binding.fromaddfoodaddMedicationNow.startAnimation(toBottom)
            binding.addfoodbottomNavViewFAB.startAnimation(rotateClose)

        }

    }
    private  fun setClicable(clicked: Boolean){
        if (!clicked){
            binding.fromaddfoodaddFoodNow.isClickable=true
            binding.fromaddfoodaddMedicationNow.isClickable=true
        }else {

            binding.fromaddfoodaddFoodNow.isClickable=false
            binding.fromaddfoodaddMedicationNow.isClickable=false
        }
    }
}