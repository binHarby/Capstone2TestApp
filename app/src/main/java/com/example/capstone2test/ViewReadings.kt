package com.example.capstone2test

import android.R
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.charts.Cartesian
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.capstone2test.databinding.FragmentViewReadingsBinding
import com.example.capstone2test.roomDatabase.ReadingViewModel
import com.example.capstone2test.roomDatabase.data.Reading
import java.util.concurrent.TimeUnit

class ViewReadings : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentViewReadingsBinding? = null
    private val binding get() = _binding!!
    var  allReading = arrayListOf<Reading>()
    var  readNewList = arrayListOf<Reading>()
    private lateinit var readingViewModel: ReadingViewModel
    private var disease :String ="blood pressure"

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
                var time: Long = read.date
                time += TimeUnit.MILLISECONDS.convert(168, TimeUnit.HOURS)
                val timeNow = System.currentTimeMillis()
                if (time > timeNow && read.diseaseName.trimEnd()==disease.trimEnd()) {
                    readNewList.add(read)
                }
            }
            if (readNewList.size>0)
            {
                setChart()
                binding.anyChartView.visibility=View.VISIBLE
                binding.progressBar.visibility=View.VISIBLE
            }
            else
            {
                binding.anyChartView.visibility=View.GONE
                binding.progressBar.visibility=View.GONE
            }

        })
    }

    private fun setChart() {



        binding.anyChartView.setProgressBar(binding.progressBar)

        binding.anyChartView.clear()

       var bar= AnyChart.bar()



        bar.setOnClickListener(object :
            ListenersInterface.OnClickListener(arrayOf("x", "value")) {
            override fun onClick(event: Event) {
            }
        })

        val data: MutableList<DataEntry> = ArrayList()

        var  sys=0
        var aio=0
        for (read in readNewList)
        {
            sys += read.sysName.toInt()
            aio += read.aioName
        }
        data.add(ValueDataEntry("SYS", sys))
        if (disease!=courses[1])
           data.add(ValueDataEntry("AIO",aio))


        bar.data(data)

        bar.title("$disease pie chart")

        bar.labels().position("outside")

        bar.legend().title().enabled(true)
        bar.legend().title()
            .text("Reading SYS & AIO For Disease")
            .padding(0.0, 0.0, 10.0, 0.0)

        bar.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER)

        binding.anyChartView.setChart(bar)



        binding.layout.setOnClickListener { }
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