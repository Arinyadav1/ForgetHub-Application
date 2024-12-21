package com.example.forget_hub.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ForgetHubData::class], version = 1, exportSchema = false)
abstract class ForgetHubDataDatabase : RoomDatabase() {

    // use all method of ForgetHubDataDAO to store and retrieve data
    abstract fun thoughtDataDAO(): ForgetHubDataDAO


    // make the room database
    companion object {
        @Volatile
        private var INSTANCE: ForgetHubDataDatabase? = null

        fun getDatabase(context: Context): ForgetHubDataDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ForgetHubDataDatabase::class.java,
                        "ForgetHubData"
                    ).build()
                }
            }
            return INSTANCE!!
        }

    }
}