package dev.kingominho.covid_19dashboard.ui.regionList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository

class RegionListViewModelFactory(
    private val dataSource: Covid19DataRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegionListViewModel::class.java)) {
            return RegionListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}