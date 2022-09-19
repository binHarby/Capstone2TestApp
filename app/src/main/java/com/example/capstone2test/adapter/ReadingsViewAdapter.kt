package com.example.capstone2test.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry

import com.anychart.charts.Cartesian

import com.anychart.data.Mapping
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.example.capstone2test.databinding.ItemReadingViewBinding
import com.example.capstone2test.roomDatabase.data.OneReading

import com.anychart.data.Set;


import com.anychart.core.cartesian.series.Line;

import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;



class ReadingsViewAdapter(private val context: Context, private val arrayList: List<OneReading>,private val numberOfValues: List<Int>) :
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


            val cartesian: Cartesian = AnyChart.line()

            cartesian.animation(true)

            cartesian.padding(10.0, 20.0, 5.0, 20.0)

            cartesian.crosshair().enabled(true)
            cartesian.crosshair()
                .yLabel(true) // TODO ystroke
                .yStroke(null as Stroke?, null, null, null as String?, null as String?)

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

            cartesian.title("Reading  for "+ reading.reading[0].diseaseName+".")
            //    cartesian.title("Reading SYS & AIO For Disease.")



           // cartesian.yAxis(0).title("Values")
         //   cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

            val seriesData: MutableList<DataEntry> = ArrayList()

            for(i in 0 until  reading.reading.size)
            {

                if (reading.reading[i].aioName>0)
                {
                    if (numberOfValues[i]>0)
                        seriesData.add(CustomDataEntry(reading.reading[i].date,reading.reading[i].sysName.toInt()/numberOfValues[i] ,reading.reading[i].aioName.toInt()/numberOfValues[i], 0  ))
                        else
                        seriesData.add(CustomDataEntry(reading.reading[i].date,reading.reading[i].sysName.toInt() ,reading.reading[i].aioName.toInt(), 0  ))

                }
                else
                {
                    if (numberOfValues[i]>0)
                    seriesData.add(ValueDataEntry(reading.reading[i].date, reading.reading[i].sysName.toInt()/numberOfValues[i] ))
                    else
                        seriesData.add(ValueDataEntry(reading.reading[i].date, reading.reading[i].sysName.toInt() ))


                }
            }
            val set = Set.instantiate()
            set.data(seriesData)
            val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
            val series2Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value2' }")


            val series1: Line = cartesian.line(series1Mapping)
            series1.name("SYS")
            series1.hovered().markers().enabled(true)
            series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4.0)

            series1.tooltip()

                .position("right")
                .anchor(Anchor.LEFT_CENTER)

                .offsetX(5.0)
                .offsetY(5.0)

            if (reading.reading[0].diseaseName=="blood pressure")
            {
                val series2: Line = cartesian.line(series2Mapping)
                series2.name("AIO")
                series2.hovered().markers().enabled(true)
                series2.color("#C82F81")
                series2.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4.0)
                series2.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5.0)
                    .offsetY(5.0)
            }





            cartesian.legend().enabled(true)
            cartesian.legend().fontSize(13.0)
            cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

            anyChartView.setChart(cartesian)


            layout.setOnClickListener { }

        }
    }


    override fun getItemCount(): Int = arrayList.size

    // Holder:
    inner class ReadingsViewHolder(val binding: ItemReadingViewBinding) : RecyclerView.ViewHolder(binding.root)

    private class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?,
        value3: Number?
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }
    }
}