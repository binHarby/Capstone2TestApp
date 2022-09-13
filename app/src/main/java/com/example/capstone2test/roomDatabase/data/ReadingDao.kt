package com.example.capstone2test.roomDatabase.data

import androidx.lifecycle.LiveData
import androidx.room.*
import org.jetbrains.annotations.NotNull


// ReadingDao contains the methods used for accessing the database, including queries.
@Dao
interface ReadingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // <- Annotate the 'addUser' function below. Set the onConflict strategy to IGNORE so if exactly the same reading exists, it will just ignore it.
    suspend fun addUser(reading: Reading)
    @NotNull
    @Update
    suspend fun updateReading(reading: Reading)
    @NotNull
    @Delete
    suspend fun deleteReading(reading: Reading)
    @NotNull
    @Query("DELETE FROM reading_table")
    suspend fun deleteAllReadingViews()
    @NotNull
    @Query("SELECT * from reading_table ORDER BY id ASC") // <- Add a query to fetch all users (in user_table) in ascending order by their IDs.
    fun readAllReadingViews(): LiveData<List<Reading>> // <- This means function return type is List. Specifically, a List of Users.
}