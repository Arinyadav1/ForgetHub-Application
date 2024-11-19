package com.example.ForgetBin.reprository

import androidx.lifecycle.LiveData
import com.example.ForgetBin.database.ThoughtData
import com.example.ForgetBin.database.ThoughtDataDAO

class DataRepo(private val thoughtDataDAO: ThoughtDataDAO) {

    val getData: LiveData<List<ThoughtData>> = thoughtDataDAO.getData()


    suspend fun insert(thoughtData: ThoughtData) {
        thoughtDataDAO.insertData(thoughtData)
    }

    suspend fun delete(thoughtData: ThoughtData) {
        thoughtDataDAO.deleteData(thoughtData)
    }

    suspend fun update(thoughtData: ThoughtData) {
        thoughtDataDAO.updateData(thoughtData)
    }

}