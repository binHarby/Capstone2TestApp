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
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import coil.load
import coil.transform.CircleCropTransformation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentAddTextBinding
import org.json.JSONException
import org.json.JSONObject


class AddText : Fragment() {
    private var _binding:FragmentAddTextBinding?=null
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
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentAddTextBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireActivity().getApplicationContext()).getToken().getToken()
        queue= VolleySingleton.getInstance(requireContext())
        binding.textbottomNavView.setOnItemReselectedListener {item ->
            when(item.itemId){
                R.id.homepage3 -> {
                    action=AddTextDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings3 -> {
                    action = AddTextDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        } 
        binding.fromtextaddFoodNow.setOnClickListener {
            action = AddTextDirections.actionGlobalAddfood()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.fromatextaddMedicationNow.setOnClickListener { 
           action=AddTextDirections.actionGlobalAddmed()
            Navigation.findNavController(binding.root).navigate(action)
            
        }
        binding.textbottomNavViewFAB.setOnClickListener { 
            onNavFabClick()
        }
        val autocompArray=resources.getStringArray(R.array.foodList).toSet().toTypedArray()
        val autocompAdapter:ArrayAdapter<String> = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1
            ,autocompArray)
        binding.textSearchView.setAdapter(autocompAdapter)
        binding.textSearchView.setOnItemClickListener { adapterView, view, position, id ->
        }
        binding.textscan.setOnClickListener {
            val query = binding.textSearchView.text
            val jsonBody = JSONObject()
            jsonBody.put("name", query)
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, URLs.URL_EXAPI_NAME, jsonBody,
                Response.Listener { response ->
                    Log.d("Name Response",response.toString())
                    //binding.barcodeSearchResultTv.text=response.toString()

                    jsonToViews(response.toString())

                },
                Response.ErrorListener { error ->
                   error.printStackTrace()
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
            binding.fromtextaddFoodNow.visibility=View.VISIBLE
            binding.fromatextaddMedicationNow.visibility=View.VISIBLE
        }else {

            binding.fromtextaddFoodNow.visibility=View.INVISIBLE
            binding.fromatextaddMedicationNow.visibility=View.INVISIBLE
        }


    }
    private  fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.fromtextaddFoodNow.startAnimation(fromBottom)
            binding.fromatextaddMedicationNow.startAnimation(fromBottom)
            binding.textbottomNavViewFAB.startAnimation(rotateOpen)
        }else {
            binding.fromtextaddFoodNow.startAnimation(toBottom)
            binding.fromatextaddMedicationNow.startAnimation(toBottom)
            binding.textbottomNavViewFAB.startAnimation(rotateClose)

        }

    }
    private  fun setClicable(clicked: Boolean){
        if (!clicked){
            binding.fromtextaddFoodNow.isClickable=true
            binding.fromatextaddMedicationNow.isClickable=true
        }else {

            binding.fromtextaddFoodNow.isClickable=false
            binding.fromatextaddMedicationNow.isClickable=false
        }
    }
    private fun jsonToViews(jsonStr:String){
        binding.textFoodProperties.removeAllViews()
        //jsonObject
        var jsonObject=JSONObject(jsonStr)



        if (jsonObject.has("error")){

            val errorProp = TextView(requireContext())
            errorProp.text="Food Can't Be Recoginized, Sorry"
            errorProp.setTextColor(requireContext().resources.getColor(R.color.white))
            errorProp.setTextSize(TypedValue.COMPLEX_UNIT_SP,42F)
            errorProp.gravity= Gravity.CENTER
            binding.textFoodProperties.addView(
                errorProp
            )
        }
        else{
            //display image
            if (jsonObject.has("meta")){
                val meta = jsonObject.get("meta") as JSONObject
                jsonObject.remove("meta")
                jsonObject.getJSONObject("general").put("serving_weight","${meta.get("def_w") as Int}g")
                if (meta.has("measures")){
                    meta.remove("measures")
                }
                if (meta.has("photo")){
                    val thumbnailUrl=meta.getJSONObject("photo").get("thumb") as String

                    binding.addtextBackground.load(thumbnailUrl) {
                        crossfade(true)
                        crossfade(1000)
                    }
                    meta.remove("photo")

                }
            }
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

                    }catch (e:JSONException){
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
                if (recom.has("report")){
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

                if (jsonObject.get(key) is JSONObject) {
                    // do something with jsonObject here
                    val insideObject=JSONObject(jsonObject.get(key).toString())
                    val key2:Iterator<String> = insideObject.keys()
                    if (key.equals("recom")){
                        continue
                    }
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

                        binding.textFoodProperties.addView(newLO
                        )
                    }
                }

            }

        }
        with(binding.addtextAmount){
            visibility= View.VISIBLE
            isClickable=true
            isFocusable=true
        }
        with(binding.addtextAnalyizwBtn){
            visibility= View.VISIBLE

            isClickable=true
            isFocusable=true
            setOnClickListener {
                val multiple = binding.addtextAmount.editText?.text.toString().trim().toDouble()
                sendToBackend(jsonObject,multiple)
            }

        }

        binding.textBriefCont.visibility=View.VISIBLE

        binding.textDetailsCont.visibility=View.VISIBLE

        binding.textReportCont.visibility=View.VISIBLE
    }
    private fun sendToBackend(jsonObject:JSONObject, multiple: Double){
        var newObject=multipleJson(jsonObject,multiple)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, URLs.URL_FOOD, newObject,
            Response.Listener { response ->
                Toast.makeText(context, "Request Success:$response", Toast.LENGTH_LONG)
                    .show()
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
                    if (!key.equals("general") && !key.equals("meta")){
                        jsonObject.getJSONObject(key).put(keyy2,(insideObject.get(keyy2) as Int)*multiple)
                    }

                }
            }

        }
        return jsonObject

    }

  
}