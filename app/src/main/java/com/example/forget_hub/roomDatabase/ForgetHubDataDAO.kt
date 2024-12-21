package com.example.forget_hub.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ForgetHubDataDAO {

    // insert data in database
    @Insert
    suspend fun insertData(forgetHubData: ForgetHubData)

    // delete data in database
    @Delete
    suspend fun deleteData(forgetHubData: ForgetHubData)

    // update data in database
    @Update
    suspend fun updateData(forgetHubData: ForgetHubData)

    // get data from database
    @Query("SELECT * FROM ForgetHubData")
    fun getData(): LiveData<List<ForgetHubData>>

}