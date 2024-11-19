package com.example.ForgetBin.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ThoughtData")
data class ThoughtData(

    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val Date: String,
)