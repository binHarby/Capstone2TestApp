package com.example.capstone2test

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Half.toFloat
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import coil.load
import coil.transform.CircleCropTransformation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentAddImageBinding
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import org.json.JSONException
import org.json.JSONObject
import uk.me.hardill.volley.multipart.MultipartRequest
import java.io.ByteArrayOutputStream


class AddImage : Fragment() {
    private var _binding:FragmentAddImageBinding?=null
    private val binding get()= _binding!!
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE= 2
    private lateinit var action: NavDirections
    private  lateinit var queue: VolleySingleton
    private var photo: Bitmap?=null
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
        _binding=FragmentAddImageBinding.inflate(inflater,container,false)
        token =  SessionManager.getInstance(requireActivity().getApplicationContext()).getToken().getToken();
        queue= VolleySingleton.getInstance(requireContext())

        binding.addimagescan.setOnClickListener {
            //0- get permission
            cameraCheckPermission()
        }
        binding.addimagefromgallery.setOnClickListener {
            galleryCheckPermission()

        }
        binding.addimagebottomNavView.setOnItemReselectedListener {item ->
            when(item.itemId){
                R.id.homepage1 -> {
                    action = AddImageDirections.actionGlobalHomepage()
                    Navigation.findNavController(binding.root).navigate(action)
                }
                R.id.settings1 -> {
                    action = AddImageDirections.actionGlobalSettingsFrag()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
            true
        }
        binding.fromaddimageaddFoodNow.setOnClickListener {
            action = AddImageDirections.actionAddImageToAddFood()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.fromaaddimageaddMedicationNow.setOnClickListener {
            action = AddImageDirections.actionAddImageToAddMed()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.addimagebottomNavViewFAB.setOnClickListener {
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
            binding.fromaddimageaddFoodNow.visibility=View.VISIBLE
            binding.fromaaddimageaddMedicationNow.visibility=View.VISIBLE
        }else {

            binding.fromaddimageaddFoodNow.visibility=View.INVISIBLE
            binding.fromaaddimageaddMedicationNow.visibility=View.INVISIBLE
        }


    }
    private  fun setAnimation(clicked: Boolean){
        if (!clicked){
            binding.fromaddimageaddFoodNow.startAnimation(fromBottom)
            binding.fromaaddimageaddMedicationNow.startAnimation(fromBottom)
            binding.addimagebottomNavViewFAB.startAnimation(rotateOpen)
        }else {
            binding.fromaddimageaddFoodNow.startAnimation(toBottom)
            binding.fromaaddimageaddMedicationNow.startAnimation(toBottom)
            binding.addimagebottomNavViewFAB.startAnimation(rotateClose)

        }

    }
    private  fun setClicable(clicked: Boolean){
        if (!clicked){
            binding.fromaddimageaddFoodNow.isClickable=true
            binding.fromaaddimageaddMedicationNow.isClickable=true
        }else {

            binding.fromaddimageaddFoodNow.isClickable=false
            binding.fromaaddimageaddMedicationNow.isClickable=false
        }
    }
    private fun galleryCheckPermission() {

        Dexter.withContext(requireContext()).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    requireContext(),
                    "You have denied the storage permission to select image",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    private fun cameraCheckPermission(){
        Dexter.withContext(requireContext())
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
            .withListener(
                object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){

                                //1- Caputre the image

                                camera()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        //1- on error, redirect to settings
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()

    }
    private fun camera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,CAMERA_REQUEST_CODE)

    }
    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(requireContext())
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                CAMERA_REQUEST_CODE -> {
                    // 2- save it as "URi"?


                     photo= data?.extras?.get("data") as Bitmap


                    //we are using coroutine image loader (coil)
                    //3- load it on to the image view "addimageSelected

                    binding.addimageBackground.load(photo) {
                        crossfade(true)
                        crossfade(1000)
                    }

                }

                GALLERY_REQUEST_CODE -> {
                    photo = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data)

                    binding.addimageBackground.load(photo) {
                        crossfade(true)
                        crossfade(1000)
                    }


                }

            }
            //send it to api
            val request: MultipartRequest = object : MultipartRequest(
                URLs.URL_EXAPI_IMG, null,
                Response.Listener { response ->
                    val json = String(
                        response.data,
                        charset(HttpHeaderParser.parseCharset(response.headers))
                    )
                    Log.d("Add Image JSON Sucess", json)
                    jsonToViews(json)
                },
                Response.ErrorListener { }) {
                //headers
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }
            }
            val ba = ByteArrayOutputStream()
            photo?.compress(Bitmap.CompressFormat.JPEG, 100, ba)
            //base64
            val imagebyte = ba.toByteArray()
            request.addPart(
                MultipartRequest.FilePart(
                    "file",
                    "mutlipart/form-data",
                    "tmp",
                    imagebyte
                )
            )

            queue.AddToRequestQueue(request)

        }

    }


    private fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var image = image
        return if (maxHeight > 0 && maxWidth > 0) {
            val width = image.width
            val height = image.height
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            image
        } else {
            image
        }
    }
    private fun jsonToViews(jsonStr:String){
        binding.addimageFoodProperties.removeAllViews()
        //jsonObject
        val jsonObject=JSONObject(jsonStr)
        if (jsonObject.has("error")){

            val errorProp = TextView(requireContext())
            errorProp.text="Food Can't Be Recoginized, Sorry"
            errorProp.setTextColor(requireContext().resources.getColor(R.color.white))
            errorProp.setTextSize(TypedValue.COMPLEX_UNIT_SP,42F)
           errorProp.gravity= Gravity.CENTER
           binding.addimageFoodProperties.addView(
               errorProp
           )
        }
        else{

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
                if (recom.has("report")&& (recom.getJSONArray("brief").length()>0) ){
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
                        newLO.layoutParams=LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
                        newLO.orientation=LinearLayout.HORIZONTAL

                        //edit prop and value
                        val keyFormated=keyy2.replace("_"," ").replaceFirstChar { it.uppercase() }
                        newProp.text=keyFormated
                        var valuePair = insideObject.get(keyy2)
                        if (!key.equals("general") && !key.equals("meta")){
                           valuePair= "$valuePair g"
                        }
                        newValue.text= valuePair.toString()
                         var lp:LinearLayout.LayoutParams= LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        lp.weight= 1F
                        newValue.layoutParams=lp

                        newProp.setTextColor(requireContext().resources.getColor(R.color.white))
                        newProp.setTextSize(TypedValue.COMPLEX_UNIT_SP,25F)
                        newProp.textAlignment=LinearLayout.TEXT_ALIGNMENT_TEXT_START
                        newValue.setTextColor(requireContext().resources.getColor(R.color.pink))
                        newValue.setTextSize(TypedValue.COMPLEX_UNIT_SP,25F)
                        newValue.textAlignment=LinearLayout.TEXT_ALIGNMENT_TEXT_END
                        //push the to int the LL
                        newLO.addView(newProp)
                        newLO.addView(newValue)
                        //push the LL to the Proprties

                        binding.addimageFoodProperties.addView(newLO
                        )
                    }
                }

            }

        binding.addimageAnalyizwBtn.setVisibility(View.VISIBLE)
            binding.addimageAnalyizwBtn.isClickable=true
            binding.addimageAnalyizwBtn.isFocusable=true
            binding.addimageAmount.setVisibility(View.VISIBLE)
            binding.addimageAmount.isClickable=true
            binding.addimageAmount.isFocusable=true
            binding.addimageAnalyizwBtn.setOnClickListener {
                val multiple = binding.addimageAmount.editText?.text.toString().trim().toDouble()
                sendToBackend(jsonStr,multiple)
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
                    if (!key.equals("general") && !key.equals("meta")){
                        jsonObject.getJSONObject(key).put(keyy2,(insideObject.get(keyy2) as Int)*multiple)
                    }

                }
            }

        }
        return jsonObject

    }

}