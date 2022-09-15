package com.example.capstone2test

import android.R
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstone2test.databinding.FragmentAddReadingsBinding
import com.example.capstone2test.roomDatabase.ReadingViewModel
import com.example.capstone2test.roomDatabase.data.Reading


class AddReadings : Fragment()  , AdapterView.OnItemSelectedListener {
    private var _binding: FragmentAddReadingsBinding? = null
    private val binding get() = _binding!!
    private var disease :String ="blood pressure"
        private lateinit var readingViewModel: ReadingViewModel
    var courses = arrayOf(
        "blood pressure", "Blood sugar"


    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddReadingsBinding.inflate(inflater, container, false)
        //code


        readingViewModel = ViewModelProvider(this)[ReadingViewModel::class.java]


        binding.readingsSpinner.onItemSelectedListener = this

        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.simple_spinner_item, courses
        )

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
            R.layout.simple_spinner_dropdown_item
        )

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        binding.readingsSpinner.adapter = ad

        binding.btnReadings.setOnClickListener { insertDataToDatabase() }

        return binding.root
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        (p0!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
        (p0.getChildAt(0) as TextView).textSize = 20f


        disease= p0.getItemAtPosition(p2) as String
        when(disease)
        {
            "Blood sugar"->
                binding.readingsOia.visibility=View.GONE
            "blood pressure"->
                binding.readingsOia.visibility=View.VISIBLE


        }



    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
    private fun insertDataToDatabase() {
       
        val readSys = binding.readingsSys.text.toString()
        var readOIA= binding.readingsOia.text.toString()
        lateinit  var reading:Reading



            // Check if the inputCheck function is true
            if (inputCheck(readSys))
            {
                if (!binding.readingsOia.isVisible ) {
                    // Create User Object
                    reading  = Reading(
                        0,
                        disease,
                        readSys,
                        System.currentTimeMillis()
                    )
                }
                else
                {
                    if (inputCheck(readOIA.toString())) {
                        // Create User Object
                        reading = Reading(
                            0,
                            disease,
                            readSys,
                             System.currentTimeMillis(),readOIA.toInt()
                        )

                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG)
                            .show()
                        return
                    }

                }
            }
        else
            {
                Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG)
                    .show()
                return
            }


        // Add Data to database
        readingViewModel.addReading(reading)
        Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
        // Navigate back
        binding.readingsSys.setText("")
        binding.readingsOia.setText("")


                }



    private fun inputCheck(readSys: String): Boolean {
        return !(TextUtils.isEmpty(readSys)  )
    }

}