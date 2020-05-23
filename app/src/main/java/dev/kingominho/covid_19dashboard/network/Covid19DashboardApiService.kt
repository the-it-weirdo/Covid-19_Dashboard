package dev.kingominho.covid_19dashboard.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.covid19api.com/"

interface Covid19DashboardApiService {
//    @GET("countries")
//    fun getCountriesAsync() : Deferred<List<Country>>

    @GET("summary")
    fun getSummaryAsync(): Deferred<SummaryContainer>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * Main entry point for network access. Call like `Network.covid19notifier.getCountries()`
 */
object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val covid19dashboard = retrofit.create(Covid19DashboardApiService::class.java)
}