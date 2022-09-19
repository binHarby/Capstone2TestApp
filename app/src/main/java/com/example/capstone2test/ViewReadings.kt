package com.example.capstone2test

import android.R
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.capstone2test.adapter.ReadingsViewAdapter
import com.example.capstone2test.databinding.FragmentViewReadingsBinding
import com.example.capstone2test.roomDatabase.ReadingViewModel
import com.example.capstone2test.roomDatabase.data.OneReading
import com.example.capstone2test.roomDatabase.data.Reading
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ViewReadings : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentViewReadingsBinding? = null
    private val binding get() = _binding!!
    var  allReading = arrayListOf<Reading>()
    var  readNewList = arrayListOf<Reading>()
    private lateinit var readingViewModel: ReadingViewModel
    private var disease :String ="blood pressure"
    private lateinit var adapter :ReadingsViewAdapter

    var courses = arrayOf(
        "blood pressure", "Blood sugar"


    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewReadingsBinding.inflate(inflater, container, false)

        //Initializing
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
        binding.readingsSpinner.onItemSelectedListener = this

        adapter= ReadingsViewAdapter(requireContext(), arrayListOf() ,arrayListOf())
        binding.rvReadings.adapter=adapter


        getData()

        return binding.root
    }

    private fun getData() {
        readingViewModel = ViewModelProvider(this)[ReadingViewModel::class.java]
        readingViewModel.readAllData.observe(viewLifecycleOwner, Observer { reading ->
            allReading.addAll(reading)
            readNewList.clear()
            for (read in reading)
            {
                var time: Long = read.date.toLong()
                time += TimeUnit.MILLISECONDS.convert(168, TimeUnit.HOURS)
                val timeNow = System.currentTimeMillis()
                if (time > timeNow && read.diseaseName.trimEnd()==disease.trimEnd()) {
                    readNewList.add(read)
                }
            }
            if (readNewList.size>0)
            {
                var result= arrayListOf<Reading>()
                var numberOfValues = arrayOf(0, 0,0,0,0,0,0)
                var now = Calendar.getInstance();
                now.add(Calendar.DATE,-6)

                result.add (Reading(0,disease,0.toString(),now.get(Calendar.DATE).toString() + "/"+ (now.get(Calendar.MONTH) + 1).toString(),0))
                 now = Calendar.getInstance();
                now.add(Calendar.DATE, -5)
                result.add (Reading(0,disease,0.toString(),now.get(Calendar.DATE).toString() + "/"+ (now.get(Calendar.MONTH) + 1).toString(),0))
                now = Calendar.getInstance();
                now.add(Calendar.DATE, -4)
                result.add (Reading(0,disease,0.toString(),now.get(Calendar.DATE).toString() + "/"+ (now.get(Calendar.MONTH) + 1).toString(),0))
                now = Calendar.getInstance();
                now.add(Calendar.DATE, -3)
                result.add (Reading(0,disease,0.toString(),now.get(Calendar.DATE).toString() + "/"+ (now.get(Calendar.MONTH) + 1).toString(),0))
                now = Calendar.getInstance();
                now.add(Calendar.DATE, -2)
                result.add (Reading(0,disease,0.toString(),now.get(Calendar.DATE).toString() + "/"+ (now.get(Calendar.MONTH) + 1).toString(),0))
                now = Calendar.getInstance();
                now.add(Calendar.DATE, -1)
                result.add (Reading(0,disease,0.toString(),now.get(Calendar.DATE).toString() + "/"+ (now.get(Calendar.MONTH) + 1).toString(),0))
                now = Calendar.getInstance();
                now.add(Calendar.DATE, 0)
                result.add (Reading(0,disease,0.toString(),now.get(Calendar.DATE).toString() + "/"+ (now.get(Calendar.MONTH) + 1).toString(),0))

                val sdf = SimpleDateFormat("dd/M")
                var  sys=0
                var aio=0
                for (read in readNewList)
                {


                            for ( i in 0 until result.size) {

                                val resultDate = Date(read.date.toLong())
                              //  Log.e("date ",result[i].date+"  "+sdf.format(resultDate) )
                                if (result[i].date == sdf.format(resultDate))
                                {
                                    ++numberOfValues[i]
                                   // Log.e("numberOfValues ","  " + numberOfValues[i])
                                    result[i].sysName  = (result[i].sysName.toInt() + read.sysName.toInt()).toString()

                                    if (read.aioName!=0)
                                        result[i].aioName  = result[i].aioName + read.aioName


                                }
                            }



                }

            var oneReading = arrayListOf<OneReading>()

                oneReading.add(OneReading(0,result))
                adapter= ReadingsViewAdapter(requireContext(),oneReading, numberOfValues.toList())
                binding.rvReadings.adapter=adapter


            }
            else
            {


            }

        })
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        (p0!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
        (p0.getChildAt(0) as TextView).textSize = 20f


        disease= p0.getItemAtPosition(p2) as String

        getData()



    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}