package com.example.capstone2test.roomDatabase.repository

import androidx.lifecycle.LiveData
import com.example.capstone2test.roomDatabase.data.Reading
import com.example.capstone2test.roomDatabase.data.ReadingDao


// Reading Repository abstracts access to multiple data sources. However this is not the part of the Architecture Component libraries.

class ReadingRepository(private val readingDao: ReadingDao) {
    val readAllData: LiveData<List<Reading>> = readingDao.readAllReadingViews()

    suspend fun addReadingView(reading: Reading) {
        readingDao.addUser(reading)
    }

    suspend fun updateReadingView(reading: Reading) {
        readingDao.updateReading(reading)
    }

    suspend fun deleteReadingView(reading: Reading) {
        readingDao.deleteReading(reading)
    }

    suspend fun deleteAllReadingViews() {
        readingDao.deleteAllReadingViews()
    }
}