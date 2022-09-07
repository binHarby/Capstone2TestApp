package com.example.capstone2test

import android.os.Bundle
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
import com.example.capstone2test.adapter.MedItemAdapter
import com.example.capstone2test.const.Layout
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentMedJournalBinding
import com.example.capstone2test.model.MedItem
import com.google.gson.Gson
import org.json.JSONArray

class MedJournal : Fragment() {
    private var _binding:FragmentMedJournalBinding?=null
    private val binding get() = _binding!!
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.roatate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }
    private var clicked:Boolean =false
    private lateinit var action: NavDirections
    private lateinit var token: String
    private  lateinit var queue: VolleySingleton
    private var gson: Gson = Gson()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentMedJournalBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireActivity().getApplicationContext()).getToken().getToken();
        queue= VolleySingleton.getInstance(requireContext())
        binding.settingsbottomNavView.setOnItemReselectedListener {item ->
            when(item.itemId){
                R.id.homepage6 -> {
                    action = MedJournalDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings6 -> {
                    action = MedJournalDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }
        binding.settingsaddFoodNow.setOnClickListener {
            action = MedJournalDirections.actionGlobalAddfood()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.settingsaddMedicationNow.setOnClickListener {
            action = MedJournalDirections.actionGlobalAddmed()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.settingsbottomNavViewFAB.setOnClickListener {
            onNavFabClick()
        }
        getMedHistory()

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
    private fun jsonArrayToMedList(jsonArrayStr:String): MutableList<MedItem> {
        val medList:MutableList<MedItem> = mutableListOf()
        val jsonArray= JSONArray(jsonArrayStr)
        for (i in 0..jsonArray.length()-1){
            val medItem: MedItem =gson.fromJson(jsonArray.getJSONObject(i).toString(), MedItem::class.java)
            medList.add(medItem)
        }
        return medList
    }
    private fun getMedHistory(){
        val jsonArrayRequest: JsonArrayRequest = object : JsonArrayRequest(
            Method.GET, URLs.URL_MED_HISTORY, null,
            Response.Listener { response ->
                if (response.length()>0) {
                    val med_list=jsonArrayToMedList(response.toString())
                    binding.journalsFoodRecyclerView.adapter= MedItemAdapter(
                        requireContext(),med_list, Layout.VERTICAL
                    )
                    binding.journalsFoodRecyclerView.setHasFixedSize(true)
                }

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
        queue.AddToRequestQueue(jsonArrayRequest)
    }

}