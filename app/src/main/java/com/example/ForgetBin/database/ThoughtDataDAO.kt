package com.example.ForgetBin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ThoughtDataDAO {

    @Insert
    suspend fun insertData(thoughtData: ThoughtData)

    @Delete
    suspend fun deleteData(thoughtData: ThoughtData)

    @Update
    suspend fun updateData(thoughtData: ThoughtData)

    @Query("SELECT * FROM ThoughtData")
    fun getData(): LiveData<List<ThoughtData>>

}