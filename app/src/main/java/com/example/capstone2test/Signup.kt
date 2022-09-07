package com.example.capstone2test

import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.capstone2test.databinding.FragmentSignupBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class Signup : Fragment() {
    private var _binding:FragmentSignupBinding?=null
    private val binding get() = _binding!!
    private lateinit var action: NavDirections
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentSignupBinding.inflate(inflater,container,false)
        //code her
        val myList = mutableListOf<Any>("Male","Female")
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_list_item, myList)
        (binding.signupGender.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val picker: MaterialDatePicker<*> = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Your Birthday")
            .build()
        picker.addOnPositiveButtonClickListener {

            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it as Long
            val format = SimpleDateFormat("yyyy-MM-dd")
            val formatted: String = format.format(utc.time)
            (binding.signupBirthday.editText as? AutoCompleteTextView)?.setText(formatted)

        }


        (binding.signupBirthday.editText as? AutoCompleteTextView)?.setOnClickListener {
            picker.show(parentFragmentManager,"tag")

        }
        binding.signupBtn.setOnClickListener {
            //check Firstname,lastname,gender,birthday if empty
            val firstName=binding.signupFirstName.editText?.text.toString()
            val lastName=binding.signupLastName.editText?.text.toString()
            val gender=binding.signupGender.editText?.text.toString()
            val birthday=binding.signupBirthday.editText?.text.toString()
            if(TextUtils.isEmpty(firstName)){
                binding.signupFirstName.error="Please enter First Name"
                binding.signupFirstName.requestFocus()
                return@setOnClickListener
            }
            binding.signupFirstName.error=""
            if(TextUtils.isEmpty(lastName)){
                binding.signupLastName.error="Please enter Last Name"
                binding.signupLastName.requestFocus()
                return@setOnClickListener
            }
            binding.signupLastName.error=""
            if(TextUtils.isEmpty(gender)){
                binding.signupGender.error="Please select your gender"
                binding.signupGender.requestFocus()
                return@setOnClickListener
            }
            binding.signupGender.error=""
            if(TextUtils.isEmpty(birthday)){
                with(binding.signupBirthday){
                    error="Please select your Birthday"
                    requestFocus()
                }
                return@setOnClickListener
            }
            binding.signupBirthday.error=""
            action = SignupDirections.continueSignup2(firstName,lastName,gender,birthday)
            Navigation.findNavController(binding.root).navigate(action)

        }


        return binding.root
    }


}