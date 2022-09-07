package com.example.capstone2test.viewmodels

import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout

class IoViewModel: ViewModel() {

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun checkViewEmpty(view: TextInputLayout,name:String): Boolean {
        if(TextUtils.isEmpty(view.editText?.text.toString())) {
            with(view) {
                error = "Please enter  ${name}"
                requestFocus()

            }
            return true
        }
        return false

    }
    fun checkViewMatchTxt(view: TextInputLayout,view2:TextInputLayout): Boolean{
        if (view.editText?.text.toString().trim().equals(view2.editText?.text.toString().trim())){
           return true
        }
        view2.error="Confirm Password doesn't match the password entered"
        return false
    }

}