package com.example.capstone2test

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentAddBarcodeBinding
import com.example.capstone2test.model.Capture
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.json.JSONException
import org.json.JSONObject


class AddBarcode : Fragment() {
    private var _binding:FragmentAddBarcodeBinding?=null
    private val binding get()= _binding!!
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.roatate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim) }
    private var clicked:Boolean =false
    private lateinit var action: NavDirections
    private lateinit var token: String
    private var upc_code:String=""
    private  lateinit var queue: VolleySingleton
    private var gson: Gson = Gson()
    private var lastId="parent"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentAddBarcodeBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireContext()).getToken().getToken();
        queue= VolleySingleton.getInstance(requireContext())
        upc_code="0"
        val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            //val context = context ?: binding.root.context
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Scanned: " + result.contents,
                    Toast.LENGTH_LONG
                ).show()
                upc_code=result.contents
                val jsonBody = JSONObject()
                jsonBody.put("upc", upc_code)
                val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, URLs.URL_EXAPI_UPC, jsonBody,
                    Response.Listener { response ->

                        Log.d("UPC Response",response.toString())
                        //binding.barcodeSearchResultTv.text=response.toString()

                        jsonToViews(response.toString())


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
        }
        binding.barcodescan.setOnClickListener {
            var capture =Capture()
               barcodeLauncher.launch(ScanOptions().setOrientationLocked(false))



        }
        binding.barcodebottomNavView.setOnItemReselectedListener {item ->
            when(item.itemId){
                R.id.homepage2 -> {
                    action = AddBarcodeDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings2 -> {
                    action = AddBarcodeDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }
        binding.frombarcodeaddFoodNow.setOnClickListener {
            action = AddBarcodeDirections.actionAddBarcodeToAddFood()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.fromabarcodeaddMedicationNow.setOnClickListener {
            action = AddBarcodeDirections.actionAddBarcodeToAddMed()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.barcodebottomNavViewFAB.setOnClickListener {
            onNavFabClick()
        }
        

        return  binding.root
    }

    private fun onNavFabClick(){
        setVisiblity(clicked)
        setAnimation(clicked)
        setClicable(clicked)
        clicked=!clicked

    }
    private fun setVisiblity(clicked:Boolean){
        if (!clicked){
            binding.frombarcodeaddFoodNow.visibility=View.VISIBLE
            binding.fromabarcodeaddMedicationNow.visibility=View.VISIBLE
        }else {

            binding.frombarcodeaddFoodNow.visibility=View.INVISIBLE
            binding.fromabarcodeaddMedicationNow.visibility=View.INVISIBLE
        }


    }
    private  fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.frombarcodeaddFoodNow.startAnimation(fromBottom)
            binding.fromabarcodeaddMedicationNow.startAnimation(fromBottom)
            binding.barcodebottomNavViewFAB.startAnimation(rotateOpen)
        }else {
            binding.frombarcodeaddFoodNow.startAnimation(toBottom)
            binding.fromabarcodeaddMedicationNow.startAnimation(toBottom)
            binding.barcodebottomNavViewFAB.startAnimation(rotateClose)

        }

    }
    private  fun setClicable(clicked: Boolean){
        if (!clicked){
            binding.frombarcodeaddFoodNow.isClickable=true
            binding.fromabarcodeaddMedicationNow.isClickable=true
        }else {

            binding.frombarcodeaddFoodNow.isClickable=false
            binding.fromabarcodeaddMedicationNow.isClickable=false
        }
    }
    private fun jsonToViews(jsonStr:String){
        //val context = context ?: binding.root.context
        binding.barcodeFoodProperties.removeAllViews()
        //jsonObject
        val jsonObject=JSONObject(jsonStr)
        if (jsonObject.has("error")){

            val errorProp = TextView(requireContext())
            errorProp.text="Food Can't Be Recoginized, Sorry"
            errorProp.setTextColor(requireContext().resources.getColor(R.color.white))
            errorProp.setTextSize(TypedValue.COMPLEX_UNIT_SP,42F)
            errorProp.gravity= Gravity.CENTER
            binding.barcodeFoodProperties.addView(
                errorProp
            )
        }
        else{
            //display image
            if (jsonObject.has("recom")){
                val recom =  jsonObject.get("recom") as JSONObject

                if (recom.has("brief")){
                    try {
                        val brief=recom.getJSONArray("brief")
                        if (brief.length()>0){
                            var briefStr:String = ""
                            for (i in 0..brief.length()-1){
                                briefStr += brief.getString(i).replace("_"," ").replaceFirstChar { it.uppercase() } + " "
                            }

                            binding.recommendationTv.text="Not Recommended"
                            binding.briefBtn.visibility=View.VISIBLE
                            binding.briefBtnTv.visibility=View.VISIBLE
                            binding.briefBtn.setOnCheckedChangeListener { _, checked ->
                                if (checked){
                                    binding.textBriefCont.visibility=View.VISIBLE
                                    binding.textBriefCont.text="Brief \nBecause Of The following food Elements:$briefStr"
                                }else{
                                    binding.textBriefCont.visibility=View.INVISIBLE
                                    binding.textBriefCont.text=""
                                }
                            }
                        }else{
                            binding.recommendationTv.text ="Recommended"
                        }

                    }catch (e: JSONException){
                        e.printStackTrace()
                    }

                }
                if (recom.has("detail")){
                    try {
                        val detail = recom.getJSONArray("detail")
                        if (detail.length()>0) {
                            var detailStr: String = "Details: \n\n"
                            var jsonInObj=JSONObject()
                            for (i in 0..detail.length()-1) {
                                jsonInObj=detail.getJSONObject(i)
                                val iter: Iterator<String> = jsonInObj.keys()
                                while (iter.hasNext()) {
                                    val key = iter.next()
                                    val value:Any= jsonInObj.get(key)
                                    detailStr+="${key.toString()}: ${value.toString()}\n"

                                }
                                if (i!=detail.length()-1){
                                    detailStr+="And \n\n"
                                }
                            }

                            binding.detailsBtn.visibility=View.VISIBLE
                            binding.detailsBtnTv.visibility=View.VISIBLE
                            binding.detailsBtn.setOnCheckedChangeListener { _, checked ->
                                if (checked){
                                    binding.textReportCont.visibility = View.VISIBLE
                                    binding.textReportCont.text =detailStr
                                }else{
                                    binding.textReportCont.visibility = View.INVISIBLE
                                    binding.textReportCont.text =""
                                }


                            }
                        }
                    }catch (e:JSONException){

                        e.printStackTrace()
                    }

                }
                if (recom.has("report") && (recom.getJSONArray("brief").length()>0)){
                    try {
                        val report = recom.getJSONArray("report")
                        if (report.length()>0) {
                            var reportStr: String = "Report: \n\n"
                            var jsonInObj=JSONObject()
                            for (i in 0..report.length()-1) {
                                jsonInObj=report.getJSONObject(i)
                                val iter: Iterator<String> = jsonInObj.keys()
                                while (iter.hasNext()) {
                                    val key = iter.next()
                                    val value:Any= jsonInObj.get(key)
                                    reportStr+="${key.toString()}: ${value.toString()}\n"

                                }
                                if (i!=report.length()-1){
                                    reportStr+="And \n\n"
                                }
                            }
                            binding.reportBtn.visibility=View.VISIBLE
                            binding.reportBtnTv.visibility=View.VISIBLE
                            binding.reportBtn.setOnCheckedChangeListener { _, checked ->
                                if (checked){
                                    binding.textReportCont.visibility = View.VISIBLE
                                    binding.textReportCont.text =reportStr
                                }else{
                                    binding.textReportCont.visibility = View.INVISIBLE
                                    binding.textReportCont.text =""
                                }


                            }

                        }
                    }catch (e:JSONException){

                        e.printStackTrace()
                    }
                }

            }

            var keys:Iterator<String>  = jsonObject.keys()
            val servUnitToSize= mutableMapOf<String,Double>()

            while (keys.hasNext()) {
                val key = keys.next()
                if (key.equals("recom")){
                    continue
                }
                if (jsonObject.get(key) is JSONObject) {
                    // do something with jsonObject here
                    val insideObject=JSONObject(jsonObject.get(key).toString())
                    val key2:Iterator<String> = insideObject.keys()
                    while (key2.hasNext()){
                        val keyy2 = key2.next()
                        Log.d("$keyy2","${insideObject.get(keyy2)}")
                        val newProp = TextView(requireContext())
                        val newValue = TextView(requireContext())
                        val newLO =  LinearLayout(requireContext())
                        newLO.layoutParams= LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
                        newLO.orientation= LinearLayout.HORIZONTAL

                        //edit prop and value
                        val keyFormated=keyy2.replace("_"," ").replaceFirstChar { it.uppercase() }
                        newProp.text=keyFormated
                        var valuePair = insideObject.get(keyy2)
                        if (!key.equals("general") && !key.equals("meta")){
                            valuePair= "$valuePair g"
                        }
                        newValue.text= valuePair.toString()
                        var lp: LinearLayout.LayoutParams= LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        lp.weight= 1F
                        newValue.layoutParams=lp

                        newProp.setTextColor(requireContext().resources.getColor(R.color.white))
                        newProp.setTextSize(TypedValue.COMPLEX_UNIT_SP,25F)
                        newProp.textAlignment= LinearLayout.TEXT_ALIGNMENT_TEXT_START
                        newValue.setTextColor(requireContext().resources.getColor(R.color.pink))
                        newValue.setTextSize(TypedValue.COMPLEX_UNIT_SP,25F)
                        newValue.textAlignment= LinearLayout.TEXT_ALIGNMENT_TEXT_END
                        //push the to int the LL
                        newLO.addView(newProp)
                        newLO.addView(newValue)
                        //push the LL to the Proprties

                        binding.barcodeFoodProperties.addView(
                            newLO
                        )
                    }
                }

            }
            with(binding.barcodeAmount){
                visibility=View.VISIBLE
                isClickable=true
                isFocusable=true
            }
            with(binding.barcodeAnalyizwBtn){
                visibility=View.VISIBLE
                isClickable=true
                isFocusable=true
                setOnClickListener {
                    val multiple = binding.barcodeAmount.editText?.text.toString().trim().toDouble()
                    sendToBackend(jsonStr,multiple)
                }
            }


        }
    }
    private fun sendToBackend(jsonStr:String, multiple: Double){
        val jsonObject=JSONObject(jsonStr)
        val newObject=multipleJson(jsonObject,multiple)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, URLs.URL_FOOD, newObject,
            Response.Listener { response ->
                jsonToViews(newObject.toString())
                Log.d("Food Post Response",response.toString())


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
    private fun multipleJson(jsonObject: JSONObject,multiple: Double): JSONObject{
        var keys:Iterator<String>  = jsonObject.keys()

        while (keys.hasNext()) {
            val key = keys.next()
            if (key.equals("recom")){
                continue
            }
            if (key.equals("general")){

                jsonObject.getJSONObject(key).put("total_cals",(jsonObject.getJSONObject(key).getInt("total_cals")).toString().toInt()*multiple)
            }
            if (jsonObject.get(key) is JSONObject) {
                // do something with jsonObject here
                val insideObject=JSONObject(jsonObject.get(key).toString())
                val key2:Iterator<String> = insideObject.keys()
                while (key2.hasNext()){
                    val keyy2 = key2.next()
                    if (!key.equals("general") && !key.equals("meta") ){
                        jsonObject.getJSONObject(key).put(keyy2,(insideObject.get(keyy2)).toString().toDouble().toInt()*multiple)
                    }

                }
            }

        }
        return jsonObject

    }





}