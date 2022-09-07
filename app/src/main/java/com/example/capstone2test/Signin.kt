package com.example.capstone2test

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.SessionManager
import com.example.capstone2test.controller.VolleySingleton
import com.example.capstone2test.databinding.FragmentSigninBinding
import com.example.capstone2test.model.User
import com.example.capstone2test.viewmodels.IoViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONException
import org.json.JSONObject
import uk.me.hardill.volley.multipart.MultipartRequest
import uk.me.hardill.volley.multipart.MultipartRequest.FormPart
import java.io.UnsupportedEncodingException
import java.text.DateFormat

class Signin : Fragment() {
    private var _binding: FragmentSigninBinding?=null
    private val binding get() =_binding!!
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var action: NavDirections
    private lateinit var gson: Gson

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentSigninBinding.inflate(inflater,container,false)
        val ioViewModel =  ViewModelProvider(this).get(IoViewModel::class.java)
        gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
            .create()
        //code here
        binding.signinBtn.setOnClickListener {
            email=  binding.signinEmail.editText?.text.toString()
            password=binding.signinPassword.editText?.text.toString()
            //check if empty
            //check if valid
            if (TextUtils.isEmpty(email)) {
                binding.signinEmail.error = "Please Enter An Email"
                binding.signinEmail.requestFocus()
                return@setOnClickListener
            } else {
                if (!ioViewModel.isEmailValid(email)) {
                    binding.signinEmail.error = "Please Enter A Valid Email"
                    binding.signinEmail.requestFocus()
                    return@setOnClickListener
                }
            }
            if (TextUtils.isEmpty(password)) {
                binding.signinEmail.error=""
                binding.signinPassword.error = "Please Enter Password"
                binding.signinPassword.requestFocus()
                return@setOnClickListener
            }
                //do somethind with password and email
            //then go to homepage
            binding.signinEmail.error=""
            binding.signinPassword.error = ""
            binding.signingProgressBar.visibility=View.VISIBLE
            //check with backend
            //RequestQueue queue = VolleySingleton.getInstance(MainActivity.this).getRequestQueue();
            val request = MultipartRequest(
                URLs.URL_LOGIN, null,
                { response ->
                    try {
                        val json =
                            String(response.data, charset(HttpHeaderParser.parseCharset(response.headers)))
                        val obj = JSONObject(json)
                        getUserInfoRequest(obj.getString("access_token"), obj)

                        //progBar.setVisibility(View.INVISIBLE);
                        //User user=new User(obj.getString("access_token"));
                        //SessionManager.getInstance(getApplicationContext()).userLogin(user);
                        //finish();
                        //startActivity(new Intent(getApplicationContext(),homepage.class));
                        //Toast.makeText(MainActivity.this, user_token, Toast.LENGTH_SHORT).show();
                        //Log.d("response",json);
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            ) {
                it.printStackTrace()
                binding.signingProgressBar.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), "No such email registered", Toast.LENGTH_LONG)
                    .show()
            }

            request.addPart(FormPart("username", email))
            request.addPart(FormPart("password", password))
            VolleySingleton.getInstance(requireContext()).AddToRequestQueue(request)



            //move to next activity
            //action =SigninDirections.signinToHomepage()
            //Navigation.findNavController(binding.root).navigate(action)


        }
        binding.signinForgotPass.setOnClickListener {
            //go to Account Recovery
            action = SigninDirections.signinToRecover()
            Navigation.findNavController(binding.root).navigate(action)

        }
        binding.signinToSignup.setOnClickListener {
            //go to Account Recovery
            action = SigninDirections.signinToSignup()
            Navigation.findNavController(binding.root).navigate(action)
        }

        return binding.root
    }
    fun getUserInfoRequest(token: String, oldResponse: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, URLs.URL_USER_SINGLE, null //, jsonBody
            , Response.Listener { response ->
                oldResponse.remove("token_type")
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
                    user.setPassword(binding.signinPassword.toString().trim { it <= ' ' })
                    SessionManager.getInstance(requireActivity().applicationContext).userLogin(user)
                    user = SessionManager.getInstance(requireActivity().applicationContext).getUser()

                    Log.d("response", nwjson.toString())
                    binding.signingProgressBar.visibility=View.INVISIBLE
                    NavToHomepage()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                binding.signingProgressBar.visibility=View.INVISIBLE
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
        action =SigninDirections.signinToHomepage()
        Navigation.findNavController(binding.root).navigate(action)
    }


}