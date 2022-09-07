package com.example.capstone2test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone2test.R
import com.example.capstone2test.const.Layout
import com.example.capstone2test.model.MedItem



class MedItemAdapter (
    private val context: Context?,
    private val medItems:List<MedItem>,
    private val layout: Int
): RecyclerView.Adapter<MedItemAdapter.MedItemAdapterViewHolder>() {

    /**
     * Initialize view elements
     */
    class MedItemAdapterViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        // TODO: Declare and initialize all of the list item UI components
        val medImageView: ImageView = view!!.findViewById(R.id.medPic)
        val medNameView: TextView =view!!.findViewById(R.id.medName)
        val medTypeNameView: TextView =view!!.findViewById(R.id.medType)
        val resNameView: TextView =view!!.findViewById(R.id.resName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedItemAdapter.MedItemAdapterViewHolder {
        // TODO: Use a conditional to determine the layout type and set it accordingly.
        //  if the layout variable is Layout.GRID the grid list item should be used. Otherwise the
        //  the vertical/horizontal list item should be used.

        //
        val flay=when (layout) {
            Layout.GRID -> R.layout.gender_list_item
            else -> R.layout.medication_item
        }
        val adaptorView: View = LayoutInflater.from(parent.context).inflate(flay,parent,false)

        // TODO Inflate the layout

        // TODO: Null should not be passed into the view holder. This should be updated to reflect
        //  the inflated layout.
        return MedItemAdapter.MedItemAdapterViewHolder(adaptorView)
    }
    override fun onBindViewHolder(holder: MedItemAdapter.MedItemAdapterViewHolder, position: Int) {
        val item = medItems[position]
        // TODO: Get the data at the current position
        // TODO: Set the image resource for the current dog
        // TODO: Set the text for the current dog's name
        // TODO: Set the text for the current dog's age
        holder.medImageView.setImageResource(R.drawable.medication_slide_icon)
        val resources = context?.resources
        holder.medNameView.text=item.medName
        holder.medTypeNameView.text=item.doseQuantType
        holder.resNameView.text=item.resName

        // TODO: Set the text for the current dog's hobbies by passing the hobbies to the
        //  R.string.dog_hobbies string constant.
        //  Passing an argument to the string resource looks like:
        //  resources?.getString(R.string.dog_hobbies, dog.hobbies)
    }
    override fun getItemCount(): Int = medItems.size // TODO: return the size of the data set instead of 0
}