package com.example.capstone2test.data

import com.example.capstone2test.R
import com.example.capstone2test.model.Journal

object JournalDataSource {
    val journals: List<Journal> = listOf(Journal(R.drawable.food1,"Food Journal"),
            Journal(R.drawable.medication1,"Medication Journal")
    )
}