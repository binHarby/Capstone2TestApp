package com.example.capstone2test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentSignup3contBinding
import com.example.capstone2test.model.User
import com.example.capstone2test.viewmodels.IoViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONException
import org.json.JSONObject
import uk.me.hardill.volley.multipart.MultipartRequest
import java.io.UnsupportedEncodingException


class Signup3cont : Fragment() {
    private var _binding: FragmentSignup3contBinding?=null
    private val binding get() = _binding!!
    private lateinit var action: NavDirections
    private lateinit var  jsonBody:JSONObject
    private var gson: Gson = Gson()
    val args:Signup3contArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentSignup3contBinding.inflate(inflater,container,false)
        val ioViewModel =  ViewModelProvider(this).get(IoViewModel::class.java)
         gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
            .create()
        binding.signup3Btn.setOnClickListener {
            if (ioViewModel.checkViewEmpty(binding.signup3Email!!,"Email")){
                return@setOnClickListener

            }
            binding.signup3Email.error=""
            if (ioViewModel.checkViewEmpty(binding.signup3Password!!,"Password")){
                return@setOnClickListener

            }
            binding.signup3Password.error=""
            if (ioViewModel.checkViewEmpty(binding.signin3PasswordConf!!,"Confirm Password")){
                return@setOnClickListener

            }
            binding.signin3PasswordConf.error=""
            if (!ioViewModel.checkViewMatchTxt(binding.signup3Password,binding.signin3PasswordConf)){
                return@setOnClickListener
            }
            binding.signin3PasswordConf.error=""
            //call backend
            signupBackend(binding.signup3Email.editText?.text.toString(),binding.signup3Password.editText?.text.toString())



            //second create goals -> POST to goals


            //third create user restrictions

            //fourth create user goals x

        }
        return binding.root
    }
    fun signupBackend(email:String,password:String){
        binding.signup3ProgressBar.visibility=View.VISIBLE
        jsonBody =JSONObject()
      //first create user -> POST to user
        jsonBody.put("first_name", args.firstName.trim())
        jsonBody.put("last_name", args.lastName.trim())
        jsonBody.put("gender", args.gender.trim())
        jsonBody.put("birthday", args.birthday.trim())
        jsonBody.put("height",args.height)
        jsonBody.put("bloodtype",args.bloodtype.trim())
        jsonBody.put("password",password.trim())
        jsonBody.put("confpassword",password.trim())
        jsonBody.put("email",email.trim())
        //do json request

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, URLs.URL_USER, jsonBody,
            Response.Listener { response ->
                Toast.makeText(requireContext(), "Request to create User Success", Toast.LENGTH_LONG)
                    .show()
                login(email,password)

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
                return headers
            }
        }
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(jsonObjectRequest)



        //third create user restrictions

        //fourth create user goals x


    }
    fun login(email:String,password: String){
        val request = MultipartRequest(
            URLs.URL_LOGIN, null,
            { response ->
                try {
                    val json =
                        String(response.data, charset(HttpHeaderParser.parseCharset(response.headers)))
                    val obj = JSONObject(json)
                    Toast.makeText(requireContext(), "Request to login Success", Toast.LENGTH_LONG)
                        .show()
                    postGeneralGoals(obj.getString("access_token"))


                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) {
            it.printStackTrace()
            Toast.makeText(requireContext(), "No such email registered", Toast.LENGTH_LONG)
                .show()
        }

        request.addPart(MultipartRequest.FormPart("username", email))
        request.addPart(MultipartRequest.FormPart("password", password))
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(request)
    }
    fun postGeneralGoals(token: String){
        //second create goals -> POST to goals
        jsonBody= JSONObject()
        jsonBody.put("weight",args.weight)
        jsonBody.put("activity_lvl",args.activityLvl)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, URLs.URL_USER_GOALS_G, jsonBody,
            Response.Listener { response ->
                Toast.makeText(requireContext(), "Request to Post general goals sucess", Toast.LENGTH_LONG)
                    .show()
                postUsrRes(token)

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
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(jsonObjectRequest)


    }
    fun postUsrRes(token:String){
        //this is empty until we can figure out how to set up  user restrictions
        postXGoals(token)
    }
    fun postXGoals(token: String){
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, URLs.URL_USER_GOALS_X, jsonBody,
            Response.Listener { response ->
                Toast.makeText(requireContext(), "Request to Post X goals sucess", Toast.LENGTH_LONG)
                    .show()
                getUserInfoRequest(token,JSONObject())

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
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(jsonObjectRequest)

    }
    fun getUserInfoRequest(token: String, oldResponse: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, URLs.URL_USER_SINGLE, null //, jsonBody
            , Response.Listener { response ->
                try {
                    Log.d("entered request","but failed")
                    val newjson = deepMerge(oldResponse, response)
                    getUserGoals(token, newjson)
                    //Toast.makeText(MainActivity.this, newjson.toString(), Toast.LENGTH_SHORT).show();
                    //Log.d("response",newjson.toString());
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                it.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Error on second request",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            //headers
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(jsonObjectRequest)
    }

    @Throws(JSONException::class)
    fun deepMerge(source: JSONObject, target: JSONObject): JSONObject {
        val keys = source.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            target.put(key, source[key])
        }
        return target
    }

    fun getUserGoals(token: String, oldResponse: JSONObject) {
        /*
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("upc", upc_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

         */
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, URLs.URL_USER_GOALS_G, null //, jsonBody
            , Response.Listener { response ->
                try {
                    response.remove("user_id")
                    val nnewjson = deepMerge(oldResponse, response)
                    getUserXGoals(token, nnewjson)

                    //Toast.makeText(MainActivity.this, nnewjson.toString(), Toast.LENGTH_SHORT).show();
                    //Log.d("response",nnewjson.toString());
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                Toast.makeText(
                    requireContext(),
                    "Error on Third request",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            //headers
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(jsonObjectRequest)
    }

    fun getUserXGoals(token: String, oldResponse: JSONObject) {
        /*
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("upc", upc_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

         */
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, URLs.URL_USER_GOALS_X, null //, jsonBody
            , Response.Listener { response ->
                try {
                    val nwjson = deepMerge(oldResponse, response)
                    gson = Gson()
                    var user: User = gson.fromJson(nwjson.toString(), User::class.java)
                    user.setPassword(binding.signup3Password.editText?.text.toString().trim())
                    SessionManager.getInstance(requireActivity().applicationContext).userLogin(user)
                    user = SessionManager.getInstance(requireActivity().applicationContext).getUser()

                    Log.d("response", nwjson.toString())
                    binding.signup3ProgressBar.visibility=View.INVISIBLE
                    NavToHomepage()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                binding.signup3ProgressBar.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), "Error on 4th request", Toast.LENGTH_SHORT).show()
            }) {
            //headers
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8"
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        VolleySingleton.getInstance(requireContext()).AddToRequestQueue(jsonObjectRequest)
    }
    fun NavToHomepage(){
        action = Signup3contDirections.signupToHomepage()
        Navigation.findNavController(binding.root).navigate(action)
    }


}