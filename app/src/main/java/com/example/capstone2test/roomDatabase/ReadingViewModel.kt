package com.example.capstone2test.roomDatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone2test.roomDatabase.data.Reading
import com.example.capstone2test.roomDatabase.data.ReadingDatabase
import com.example.capstone2test.roomDatabase.repository.ReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// UserViewModel provides users data to the UI and survive configuration changes.
// A ViewModel acts as a communication center between the Repository and the UI.

class ReadingViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Reading>>
    private val repository: ReadingRepository

    init {
        val readingDao = ReadingDatabase.getDatabase(application).userDao()
        repository = ReadingRepository(readingDao)
        readAllData = repository.readAllData
    }

    fun addReading(reading: Reading) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReadingView(reading)
        }
    }

    fun updateUser(reading: Reading) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReadingView(reading)
        }
    }

    fun deleteUser(reading: Reading) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReadingView(reading)
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllReadingViews()
        }
    }
}