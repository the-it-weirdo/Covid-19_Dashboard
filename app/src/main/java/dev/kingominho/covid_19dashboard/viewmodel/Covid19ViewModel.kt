package dev.kingominho.covid_19dashboard.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.kingominho.covid_19dashboard.database.getDatabase
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository
import dev.kingominho.covid_19dashboard.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class Covid19ViewModel(application: Application) : AndroidViewModel(application) {
    //used in MainActivity

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val covid19Repository = Covid19DataRepository(getDatabase(application))

    private val _application = application

    init {
        Log.d("Tag", "Covid19ViewModel Initialising")
        refreshData()
    }

    fun refreshData() {
        val networkConnected = isNetworkAvailable(_application) ?: false
        if (networkConnected) {
            refreshDataFromRepository()
        }
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                covid19Repository.refreshSummaries()
            } catch (error: Exception) {
                Log.e("Tag", "Covid19ViewModel Error: ", error)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(Covid19ViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return Covid19ViewModel(app) as T
            }
            throw IllegalArgumentException("Invalid ViewModel class")
        }
    }

}