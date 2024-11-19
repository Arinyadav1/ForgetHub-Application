package com.example.ForgetBin.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ForgetBin.database.ThoughtData
import com.example.ForgetBin.reprository.DataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DataViewModel(private val repository: DataRepo) : ViewModel() {

    val getData: LiveData<List<ThoughtData>> = repository.getData

    fun insert(thoughtData: ThoughtData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(thoughtData)
        }
    }

    fun delete(thoughtData: ThoughtData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(thoughtData)
        }
    }

    fun update(thoughtData: ThoughtData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(thoughtData)
        }
    }
}