package com.example.capstone2test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.capstone2test.HomepageDirections
import com.example.capstone2test.R
import com.example.capstone2test.const.Layout
import com.example.capstone2test.data.JournalDataSource

class JournalThumbnailsAdapter(
    private val context: Context?,
    private val layout: Int,
    private val viewpager: ViewPager2
): RecyclerView.Adapter<JournalThumbnailsAdapter.JournalThumbnailsViewHolder>(){
    private var journalsList=JournalDataSource.journals.toMutableList()

    /**
     * Initialize view elements
     */
    class JournalThumbnailsViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        // TODO: Declare and initialize all of the list item UI components
        val journalImageView: ImageView = view!!.findViewById(R.id.jounralPic)
        val journalNameView: TextView =view!!.findViewById(R.id.journalName)
        val journalLayout: View = view!!.findViewById(R.id.slide_layout_view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalThumbnailsViewHolder {
        // TODO: Use a conditional to determine the layout type and set it accordingly.
        //  if the layout variable is Layout.GRID the grid list item should be used. Otherwise the
        //  the vertical/horizontal list item should be used.

        //
        val flay=when (layout) {
            Layout.GRID -> R.layout.gender_list_item
            else -> R.layout.journal_thumbnail_item
        }
        val adaptorView: View = LayoutInflater.from(parent.context).inflate(flay,parent,false)

        // TODO Inflate the layout

        // TODO: Null should not be passed into the view holder. This should be updated to reflect
        //  the inflated layout.
        return JournalThumbnailsViewHolder(adaptorView)
    }
    override fun onBindViewHolder(holder: JournalThumbnailsAdapter.JournalThumbnailsViewHolder, position: Int) {
        if (position == journalsList.size - 2){
            viewpager.post(runnable)
        }
        val item = journalsList[position]
        // TODO: Get the data at the current position
        // TODO: Set the image resource for the current dog
        // TODO: Set the text for the current dog's name
        // TODO: Set the text for the current dog's age
        holder.journalImageView.setImageResource(item.imageResourceId)

        val resources = context?.resources
        holder.journalNameView.text=item.journalName
        if (item.journalName.equals("Food Journal")){
            holder.journalLayout.setOnClickListener {

                val action = HomepageDirections.actionGlobalFoodJournal()
                holder.itemView.findNavController().navigate(action)
            }
        }else{
            holder.journalLayout.setOnClickListener {
                val action = HomepageDirections.actionGlobalMedJournal()
                holder.itemView.findNavController().navigate(action)
            }
        }


        // TODO: Set the text for the current dog's hobbies by passing the hobbies to the
        //  R.string.dog_hobbies string constant.
        //  Passing an argument to the string resource looks like:
        //  resources?.getString(R.string.dog_hobbies, dog.hobbies)
    }
    override fun getItemCount(): Int = journalsList.size // TODO: return the size of the data set instead of 0
    private val runnable = Runnable {
        journalsList.addAll(journalsList)
        notifyDataSetChanged()

    }

}