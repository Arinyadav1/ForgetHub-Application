package com.vobot.forgethub.viewModel

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vobot.forgethub.roomDatabase.ForgetHubData
import com.vobot.forgethub.reprository.DataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime


class DataViewModel(private val repository: DataRepo, context: Context) : ViewModel() {

    //background Image manage on Note screen
    fun image(): Int {
        return repository.backgroundImage.random()
    }

    //manage navigation drawer
    val useOpenCloseNavigationDrawerState = MutableLiveData(false)
    fun updateOpenCloseNavigationDrawerState(trigger: Boolean) {
        useOpenCloseNavigationDrawerState.value = trigger
    }

    //manage vibration
    @Suppress("DEPRECATION")
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    fun vibration() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            }
        }
    }

    //delete item box id and title and description for share data
    val itemIdForDeleteItem = MutableLiveData(0)
    val itemDataForShareData = MutableLiveData("")
    fun updateShareDataAndDeleteItemId(id: Int, data: String) {
        itemIdForDeleteItem.value = id
        itemDataForShareData.value = data
    }


    //show hide delete icon without rendering whole MainScreen function
    val useDeleteIconOnHomeScreeState = MutableLiveData(false)
    fun updateEnableDisableDeleteIconOnHomeScreenState(trigger: Boolean) {
        useDeleteIconOnHomeScreeState.value = trigger
    }

    //change colours of screen
    val backgroundColor =
        MutableLiveData(repository.backgroundColor[LocalTime.now().hour % repository.backgroundColor.size])


    // it manage scroll state of vertical staggered grid when navigate one screen to another
    var scrollState by mutableStateOf(LazyStaggeredGridState())
        private set

    fun updateScrollState(state: LazyStaggeredGridState) {
        scrollState = state
    }

    //room database get data
    val getData: LiveData<List<ForgetHubData>> = repository.getData


    //room database insert data
    fun insert(forgetHubData: ForgetHubData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(forgetHubData)
        }
    }

    //room database delete data
    fun delete(forgetHubData: ForgetHubData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(forgetHubData)
        }
    }

    //room database update data
    fun update(forgetHubData: ForgetHubData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(forgetHubData)
        }
    }
}