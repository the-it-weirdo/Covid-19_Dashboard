package dev.kingominho.covid_19dashboard.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.kingominho.covid_19dashboard.database.Covid19RoomDatabase
import dev.kingominho.covid_19dashboard.database.asDomainCountryModel
import dev.kingominho.covid_19dashboard.database.asDomainCountrySummary
import dev.kingominho.covid_19dashboard.domain.DomainCountry
import dev.kingominho.covid_19dashboard.domain.DomainCountrySummary
import dev.kingominho.covid_19dashboard.network.Network
import dev.kingominho.covid_19dashboard.network.asCountriesSummaryDatabaseModel
import dev.kingominho.covid_19dashboard.network.asCountriesSummaryDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class Covid19DataRepository(private val database: Covid19RoomDatabase) {

//    val countries: LiveData<List<DomainCountry>> =
//        Transformations.map(database.countryDao.getCountries()) {
//            Log.d("Tag", "From Database: Country List: ${it[0].countryName}")
//            it.asCountryDomainModel()
//        }

    val countries: LiveData<List<DomainCountry>> =
        Transformations.map(database.summaryDao.getAllCountries()) {
            it.asDomainCountryModel()
        }

//    val countrySummaries: LiveData<List<DomainCountrySummary>> =
//        Transformations.map(database.summaryDao.getAllSummary()) {
//            Log.d("Tag", "From Database: Summary List:  ${it[0].countryName}")
//            it.asCountriesSummaryDomainModel()
//        }

    fun getSummary(slug:String) : LiveData<DomainCountrySummary> {
        Log.d("Tag", "Slug for Database query in repository = $slug")
        return Transformations.map(database.summaryDao.getCountrySummary(slug)) {
            Log.d("Tag", "From Database: Summary of: ${it?.countryName}")
            it?.asDomainCountrySummary()
        }
    }

    suspend fun summaryFromNetwork(): MutableLiveData<List<DomainCountrySummary>> {
        val rr: MutableLiveData<List<DomainCountrySummary>> = MutableLiveData()
        withContext(Dispatchers.IO) {
            try {
                val ll = Network.covid19dashboard.getSummaryAsync().await()
                rr.postValue(ll.asCountriesSummaryDomainModel())
            } catch (e: Exception) {
                Log.e("Tag", "Network Exception from Repository: ", e)
            }
        }
        return rr
    }

    /**
     * Refresh functions for offline database
     * */

    suspend fun refreshSummaries() {
        withContext(Dispatchers.IO) {
            try {
                val summaries = Network.covid19dashboard.getSummaryAsync().await()
                val mm = summaries.Countries[0].countryName
                Log.d("Tag:", "From Network $mm")
                database.summaryDao.insertAllCountrySummaries(*summaries.asCountriesSummaryDatabaseModel())
            } catch (exception: HttpException) {
                Log.e("Tag", "Repository HttpException: ", exception)
            } catch (exception: Exception) {
                Log.e("Tag", "Repository Exception: ", exception)
            }
        }
    }
}