package com.example.forget_hub.reprository

import androidx.lifecycle.LiveData
import com.example.forget_hub.model.DataOfUi
import com.example.forget_hub.roomDatabase.ForgetHubData
import com.example.forget_hub.roomDatabase.ForgetHubDataDAO

class DataRepo(private val forgetHubDataDAO: ForgetHubDataDAO, dataOfUi: DataOfUi) {

    // get data from database
    val getData: LiveData<List<ForgetHubData>> = forgetHubDataDAO.getData()


    // insert data in database
    suspend fun insert(forgetHubData: ForgetHubData) {
        forgetHubDataDAO.insertData(forgetHubData)
    }

    // delete data in database
    suspend fun delete(forgetHubData: ForgetHubData) {
        forgetHubDataDAO.deleteData(forgetHubData)
    }

    // update data in database
    suspend fun update(forgetHubData: ForgetHubData) {
        forgetHubDataDAO.updateData(forgetHubData)
    }

    // change header and other part of colours
    val backgroundColor = dataOfUi.backgroundColourChange()

    // use image in firstTimeDataSaveScreen
    val backgroundImage = dataOfUi.image()

}