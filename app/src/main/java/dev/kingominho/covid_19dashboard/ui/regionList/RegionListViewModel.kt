package dev.kingominho.covid_19dashboard.ui.regionList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kingominho.covid_19dashboard.repository.Covid19DataRepository

class RegionListViewModel(
    dataSource: Covid19DataRepository,
    application: Application
): ViewModel() {

    val repository = dataSource

    val countries = repository.countries

    private var _selectedCountrySlug = ""
    val selectedCountrySlug: String
        get() = _selectedCountrySlug

    private val _eventCountryClicked = MutableLiveData<Boolean>()
    val eventCountrySelected: LiveData<Boolean>
        get() = _eventCountryClicked

    fun onCountryClicked(slug: String) {
        _selectedCountrySlug = slug
        Log.d("Tag", "Selected slug = $slug")
        _eventCountryClicked.value = true
    }

    fun countrySelectEventComplete() {
        _eventCountryClicked.value = false
    }

}
