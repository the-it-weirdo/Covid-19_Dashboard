package dev.kingominho.covid_19dashboard.ui.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kingominho.covid_19dashboard.domain.DomainCountrySummary
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository
import dev.kingominho.covid_19dashboard.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardViewModel(
    dataSource: Covid19DataRepository,
    application: Application
) : ViewModel() {

    val repository = dataSource

    private var viewModelJob = Job()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _refreshedData = MutableLiveData(false)
    val refreshedData: LiveData<Boolean>
        get() = _refreshedData


    private val _application: Application = application

    private fun setRefreshDataComplete(value: Boolean) {
        _refreshedData.value = value
    }

    fun refreshData() {
        val networkConnected = isNetworkAvailable(_application) ?: false
        if (_refreshedData.value == false && networkConnected) {
            refreshDataFromRepository()
        }
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                repository.refreshSummaries()
                setRefreshDataComplete(true)
            } catch (error: Exception) {
                Log.e("Tag", "From Dashboard data refresh error: ", error)
                setRefreshDataComplete(false)
            }
        }
    }

    private var _selectedCountry = repository.getSummary("global")
    val selectedCountry: LiveData<DomainCountrySummary>
        get() = _selectedCountry

    fun changeSelectedCountry(slug: String) {
        _selectedCountry = repository.getSummary(slug)
    }

    private val _eventButtonClicked = MutableLiveData<Boolean>()
    val eventButtonClicked: LiveData<Boolean>
        get() = _eventButtonClicked

    fun onButtonClicked() {
        _eventButtonClicked.value = true
    }

    fun buttonClickEventComplete() {
        _eventButtonClicked.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}