package com.example.capstone2test

import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentAddMedBinding
import com.example.capstone2test.viewmodels.IoViewModel
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class AddMed : Fragment() {
    private var _binding:FragmentAddMedBinding?=null
    private val binding get() = _binding!!
    private lateinit var action: NavDirections
    private  lateinit var queue: VolleySingleton
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.roatate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }
    private var clicked:Boolean =false
    private lateinit var token: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentAddMedBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireActivity().getApplicationContext()).getToken().getToken();
        queue= VolleySingleton.getInstance(requireContext())
        val autocompArray=resources.getStringArray(R.array.drugList).toSet().toTypedArray()
        val autocompAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1
            ,autocompArray)
        (binding.medicationName.editText as AutoCompleteTextView).setAdapter(autocompAdapter)
        val ioViewModel =  ViewModelProvider(this).get(IoViewModel::class.java)
        binding.medicationbottomNavView.setOnItemReselectedListener {
                item ->
            when(item.itemId){
                R.id.homepage4 -> {
                    action=AddMedDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings4 -> {
                    action = AddMedDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }
        binding.medicationbottomNavViewFAB.setOnClickListener {
            onNavFabClick()
        }
        binding.frommedicationaddFoodNow.setOnClickListener {
            action = AddMedDirections.actionGlobalAddfood()
            Navigation.findNavController(binding.root)
        }
        binding.fromamedicationaddMedicationNow.setOnClickListener {

            action = AddMedDirections.actionGlobalAddmed()
            Navigation.findNavController(binding.root)
        }
        binding.medicationscan.setOnClickListener {
            with(binding){
                if (ioViewModel.checkViewEmpty(medicationName,"Medication Name")
                    || ioViewModel.checkViewEmpty(medicationDailyGoal,"Recommended Daily Intake")
                    || ioViewModel.checkViewEmpty(medicationDoseUnit,"Dose Unit")
                    || ioViewModel.checkViewEmpty(medicaitonIllness,"Related Illness")

                ){
                    return@setOnClickListener

                }
                registerMed(medicationName.editText?.text.toString().trim(),medicationDailyGoal.editText?.text.toString().toInt()
                ,medicationDoseUnit.editText?.text.toString().trim(),medicaitonIllness.editText?.text.toString().trim())

            }
        }

        return binding.root
    }
    private fun registerMed(medName:String,medDaily:Int,medDose:String,medIll:String){
        val jsonBody = JSONObject()
        jsonBody.put("med_name", medName)
        jsonBody.put("res_name", medIll)
        jsonBody.put("dose_quant", medDaily)
        jsonBody.put("dose_quant_type", medDose)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, URLs.URL_MED, jsonBody,
            Response.Listener { response ->

                Log.d("UPC Response",response.toString())
                //binding.barcodeSearchResultTv.text=response.toString()

                revealSuccess(medName,response.toString())
                /*
                for (prop in UpcFoodItem::class.memberProperties){
                    Log.d("${prop.name}","${prop.get(upcResult)}")
                    if (prop.get(upcResult)!=null){
                        //val newProp=TextView(requireContext())
                        //newProp.text="${prop}"


                        //binding.barcodeFoodProperties.addView()
                    }
                }
                */

            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireContext(),
                    error.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }) {
            //headers
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8"
                headers["token"] = "Bearer ${token}"
                headers["Authorization"] = "Bearer ${token}"
                return headers
            }
        }
        queue.AddToRequestQueue(jsonObjectRequest)


    }
    private fun revealSuccess(medName:String,jsonStr:String){
        binding.medicationResultTv.visibility=View.VISIBLE
        val jsonObject=JSONObject(jsonStr)
        if (jsonObject.has("error")){
            val error=jsonObject.get("error")
            binding.medicationResultTv.text="Error: ${error}"
            binding.medicationResultTv.setTextColor(requireContext().resources.getColor(R.color.red))
        }else {
            binding.medicationResultTv.text=requireContext().resources.getString(R.string.medication_result_tv,medName)
        }

        Timer().schedule(3000) {
            binding.medicationResultTv.setTextColor(requireContext().resources.getColor(R.color.lightGreen))
            binding.medicationResultTv.visibility=View.INVISIBLE
        }
    }

    private fun onNavFabClick(){
        setVisiblity(clicked)
        setAnimation(clicked)
        setClicable(clicked)
        clicked=!clicked

    }
    private fun setVisiblity(clicked:Boolean){
        if (!clicked){
            binding.frommedicationaddFoodNow.visibility=View.VISIBLE
            binding.fromamedicationaddMedicationNow.visibility=View.VISIBLE
        }else {

            binding.frommedicationaddFoodNow.visibility=View.INVISIBLE
            binding.fromamedicationaddMedicationNow.visibility=View.INVISIBLE
        }


    }
    private  fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.frommedicationaddFoodNow.startAnimation(fromBottom)
            binding.fromamedicationaddMedicationNow.startAnimation(fromBottom)
            binding.medicationbottomNavViewFAB.startAnimation(rotateOpen)
        }else {
            binding.frommedicationaddFoodNow.startAnimation(toBottom)
            binding.fromamedicationaddMedicationNow.startAnimation(toBottom)
            binding.medicationbottomNavViewFAB.startAnimation(rotateClose)

        }

    }
    private  fun setClicable(clicked: Boolean){
        if (!clicked){
            binding.frommedicationaddFoodNow.isClickable=true
            binding.fromamedicationaddMedicationNow.isClickable=true
        }else {

            binding.frommedicationaddFoodNow.isClickable=false
            binding.fromamedicationaddMedicationNow.isClickable=false
        }
    }
}