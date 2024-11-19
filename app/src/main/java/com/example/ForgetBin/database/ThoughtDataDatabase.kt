package com.example.ForgetBin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ThoughtData::class], version = 1)
abstract class ThoughtsDataDatabase : RoomDatabase() {

    abstract fun thoughtDataDAO(): ThoughtDataDAO

    companion object {
        @Volatile
        private var INSTANCE: ThoughtsDataDatabase? = null

        fun getDatabase(context: Context): ThoughtsDataDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, ThoughtsDataDatabase::class.java, "ThoughtDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }

    }
}