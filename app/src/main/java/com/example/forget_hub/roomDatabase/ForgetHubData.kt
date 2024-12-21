package com.example.forget_hub.roomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ForgetHubData")
data class ForgetHubData(

    // types of data to save in database
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val date: String,
)