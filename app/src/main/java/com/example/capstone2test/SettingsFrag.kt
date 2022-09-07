package com.example.capstone2test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.adapter.UpcFoodItemAdapter
import com.example.capstone2test.const.Layout
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentSettingsBinding

class SettingsFrag : Fragment() {
    private var _binding:FragmentSettingsBinding?=null
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
        _binding=FragmentSettingsBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireActivity().getApplicationContext()).getToken().getToken();
        queue= VolleySingleton.getInstance(requireContext())
        val user = SessionManager.getInstance(requireActivity().getApplicationContext()).user
        binding.settingsFirstName.text=user.firstName
        binding.settingsLastName.text =user.lastName
        binding.settingsDOB.text = user.birthday.toString()
        binding.settingsGender.text = user.gender
        binding.settingsActivity.text = user.activiyLvl.toString()
        binding.settingsEmail.text = user.email
        binding.settingsbottomNavView.setOnItemReselectedListener { item ->
            when(item.itemId){
                R.id.homepage0 -> {
                    action = SettingsFragDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.more0 -> {
                    action = SettingsFragDirections.actionGlobalMore()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }
        binding.settingsbottomNavViewFAB.setOnClickListener {
            onNavFabClick()
        }
        binding.settingsaddFoodNow.setOnClickListener {
            action = SettingsFragDirections.actionSettingsFragToAddFood()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.settingsaddMedicationNow.setOnClickListener {
            action = SettingsFragDirections.actionSettingsFragToAddMed()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.settingsReset.setOnClickListener {
               resetState()
        }
        return binding.root
    }
    private fun resetState(){
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, URLs.URL_STATE_RESET, null,
            Response.Listener { response ->
              Log.d("Reset State","Success")

            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show()
                error.printStackTrace()
            }) {
            //headers
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8"
                headers["Authorization"] = "Bearer ${token}"
                headers["token"] = "Bearer ${token}"
                return headers
            }
        }
        queue.AddToRequestQueue(jsonObjectRequest)
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