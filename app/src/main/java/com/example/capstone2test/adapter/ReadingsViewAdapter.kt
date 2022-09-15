package com.example.capstone2test.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.capstone2test.databinding.ItemReadingViewBinding
import com.example.capstone2test.roomDatabase.data.Reading



class ReadingsViewAdapter(private val context: Context, private val arrayList: List<Reading>) :
    RecyclerView.Adapter<ReadingsViewAdapter.ReadingsViewHolder>()
{


    // Adapter:
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingsViewHolder {
        // Initializing:
        val inflater = LayoutInflater.from(context)
        val binding = ItemReadingViewBinding.inflate(inflater, parent, false)
        // Returning:
        return ReadingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReadingsViewHolder, position: Int) {
        // Initializing:
        val reading = arrayList[position]
        // Prepare:
        holder.binding.apply {
            // Setting:
            anyChartView.setProgressBar(progressBar)



            val pie = AnyChart.bar()

            pie.setOnClickListener(object :
                ListenersInterface.OnClickListener(arrayOf("x", "value")) {
                override fun onClick(event: Event) {
                    //   Toast.makeText(context, event.getData().get("x").toString() + ":" + event.data.get("value"), Toast.LENGTH_SHORT).show()
                }
            })

            val data: MutableList<DataEntry> = ArrayList()
            data.add(ValueDataEntry("SYS", reading.sysName.toInt()))
            if (reading.aioName>0)
            data.add(ValueDataEntry("AIO", reading.aioName))


            pie.data(data)

            pie.title(reading.diseaseName+" pie chart")

            pie.labels().position("outside")

            pie.legend().title().enabled(true)
            pie.legend().title()
                .text("Reading SYS & AIO For Disease")
                .padding(0.0, 0.0, 10.0, 0.0)

            pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER)

            anyChartView.setChart(pie)

            layout.setOnClickListener { }

        }
    }


    override fun getItemCount(): Int = arrayList.size

    // Holder:
    inner class ReadingsViewHolder(val binding: ItemReadingViewBinding) : RecyclerView.ViewHolder(binding.root)


}