package com.example.capstone2test.roomDatabase.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


 class OneReading(

    val id: Int, // <- 'id' is the primary key which will be autogenerated by the Room library.
    val reading: List<Reading>,

)