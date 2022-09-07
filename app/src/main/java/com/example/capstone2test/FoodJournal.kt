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
import com.example.capstone2test.adapter.UpcFoodItemAdapter
import com.example.capstone2test.const.Layout
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentFoodJournalBinding
import com.example.capstone2test.model.UpcFoodItem
import com.google.gson.Gson
import org.json.JSONArray

class FoodJournal : Fragment() {
    private var _binding:FragmentFoodJournalBinding?=null
    private val binding get() =_binding!!
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
        _binding=FragmentFoodJournalBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireActivity().getApplicationContext()).getToken().getToken();
        queue= VolleySingleton.getInstance(requireContext())
        binding.settingsbottomNavView.setOnItemReselectedListener {item ->
            when(item.itemId){
                R.id.homepage5 -> {
                    action = FoodJournalDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings5 -> {
                    action = FoodJournalDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
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
        getFoodHistory()

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
    private fun jsonArrayToUpcList(jsonArrayStr:String): MutableList<UpcFoodItem> {
        val upcList:MutableList<UpcFoodItem> = mutableListOf()
        val jsonArray=JSONArray(jsonArrayStr)
        for (i in 0..jsonArray.length()-1){
            val upcItem: UpcFoodItem =gson.fromJson(jsonArray.getJSONObject(i).toString(), UpcFoodItem::class.java)
            upcList.add(upcItem)
        }
        return upcList
    }
    private fun getFoodHistory(){
        val jsonArrayRequest: JsonArrayRequest = object : JsonArrayRequest(
            Method.GET, URLs.URL_FOOD_HISTORY, null,
            Response.Listener { response ->
              if (response.length()>0) {
                  val upc_list=jsonArrayToUpcList(response.toString())
                  binding.journalsFoodRecyclerView.adapter=UpcFoodItemAdapter(
                      requireContext(),upc_list, Layout.VERTICAL
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