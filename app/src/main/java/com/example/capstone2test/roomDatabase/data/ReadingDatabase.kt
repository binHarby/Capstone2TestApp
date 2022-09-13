package com.example.capstone2test.roomDatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// ReadingDatabase represents database and contains the database holder and server the main access point for the underlying connection to your app's persisted, relational data.

@Database(
    entities = [Reading::class],
    version = 1,                // <- Database version
    exportSchema = true
)
abstract class ReadingDatabase: RoomDatabase() { // <- Add 'abstract' keyword and extends roomDatabase
    abstract fun userDao(): ReadingDao

    companion object {
        @Volatile
        private var INSTANCE: ReadingDatabase? = null

        fun getDatabase(context: Context): ReadingDatabase{
            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ReadingDatabase::class.java,
                    "reading_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}