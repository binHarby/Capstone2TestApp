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
import com.example.capstone2test.data.JournalDataSource
import com.example.capstone2test.model.UpcFoodItem

class UpcFoodItemAdapter (
    private val context: Context?,
    private val UpcFoodItems:List<UpcFoodItem>,
    private val layout: Int
): RecyclerView.Adapter<UpcFoodItemAdapter.UpcFoodItemViewHolder>() {

        /**
         * Initialize view elements
         */
        class UpcFoodItemViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
            // TODO: Declare and initialize all of the list item UI components
            val upcFoodlImageView: ImageView = view!!.findViewById(R.id.upcFoodPic)
            val upcFoodlNameView: TextView =view!!.findViewById(R.id.upcFoodName)
            val upcBrandNameView: TextView =view!!.findViewById(R.id.upcBrandName)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcFoodItemAdapter.UpcFoodItemViewHolder {
            // TODO: Use a conditional to determine the layout type and set it accordingly.
            //  if the layout variable is Layout.GRID the grid list item should be used. Otherwise the
            //  the vertical/horizontal list item should be used.

            //
            val flay=when (layout) {
                Layout.GRID -> R.layout.gender_list_item
                else -> R.layout.upc_food_item
            }
            val adaptorView: View = LayoutInflater.from(parent.context).inflate(flay,parent,false)

            // TODO Inflate the layout

            // TODO: Null should not be passed into the view holder. This should be updated to reflect
            //  the inflated layout.
            return UpcFoodItemAdapter.UpcFoodItemViewHolder(adaptorView)
        }
        override fun onBindViewHolder(holder: UpcFoodItemAdapter.UpcFoodItemViewHolder, position: Int) {
            val item = UpcFoodItems[position]
            // TODO: Get the data at the current position
            // TODO: Set the image resource for the current dog
            // TODO: Set the text for the current dog's name
            // TODO: Set the text for the current dog's age
            holder.upcFoodlImageView.setImageResource(R.drawable.food_serrving_img)
            val resources = context?.resources
            holder.upcFoodlNameView.text=item.general.foodName
            holder.upcBrandNameView.text=item.general.brandName

            // TODO: Set the text for the current dog's hobbies by passing the hobbies to the
            //  R.string.dog_hobbies string constant.
            //  Passing an argument to the string resource looks like:
            //  resources?.getString(R.string.dog_hobbies, dog.hobbies)
        }
        override fun getItemCount(): Int = UpcFoodItems.size // TODO: return the size of the data set instead of 0
    }
