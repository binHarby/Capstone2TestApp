package com.example.capstone2test.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.capstone2test.const.URLs
import com.example.capstone2test.controller.VolleySingleton
import org.json.JSONException

class calStateViewModel(app: Application): AndroidViewModel(app) {
    private val _calState = MutableLiveData<Int>()
    fun calState(): LiveData<Int>{
        return _calState
    }
    fun getUserState(token:String) {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, URLs.URL_STATE_GET, null //, jsonBody
            , Response.Listener { response ->
                try {
                    _calState.value=response.getJSONObject("general").getInt("total_cals")
                    //Log.d("Message",consumedCals.toString())
                    //response is the answer
                    //Toast.makeText(MainActivity.this, newjson.toString(), Toast.LENGTH_SHORT).show();
                    //Log.d("response",newjson.toString());
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                it.printStackTrace()

            }) {
            //headers
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8"
                headers["Authorization"] = "Bearer ${token}"
                return headers
            }
        }
        VolleySingleton.getInstance(getApplication<Application?>().applicationContext).AddToRequestQueue(jsonObjectRequest)
    }

}