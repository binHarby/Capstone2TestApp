package com.example.capstone2test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone2test.roomDatabase.data.Reading
import com.example.capstone2test.databinding.ItemReadingViewBinding


class ReadingsViewAdapter(private val context: Context, private val arrayList: List<Reading>) :
    RecyclerView.Adapter<ReadingsViewAdapter.ReadingsViewHolder>() {

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
            diseaseName.text = reading.diseaseName
            sysName.text = reading.sysName

            if(reading.aioName!=null)
            {
                oiaName.visibility= View.VISIBLE
                oiaName.text = reading.aioName
            }


            layout.setOnClickListener {



            }

        }
    }

    override fun getItemCount(): Int = arrayList.size

    // Holder:
    inner class ReadingsViewHolder(val binding: ItemReadingViewBinding) : RecyclerView.ViewHolder(binding.root)
}