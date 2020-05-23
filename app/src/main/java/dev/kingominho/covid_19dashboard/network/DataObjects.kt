package dev.kingominho.covid_19dashboard.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.kingominho.covid_19dashboard.database.DatabaseCountry
import dev.kingominho.covid_19dashboard.database.DatabaseCountrySummary
import dev.kingominho.covid_19dashboard.domain.DomainCountrySummary

@JsonClass(generateAdapter = true)
data class SummaryContainer(
    val Global: Global,
    val Countries: List<CountrySummary>,
    val Date: String
)

@JsonClass(generateAdapter = true)
data class Global(

    @Json(name = "NewConfirmed")
    val newConfirmed: Int = 0,

    @Json(name = "TotalConfirmed")
    val totalConfirmed: Int = 0,

    @Json(name = "NewDeaths")
    val newDeaths: Int = 0,

    @Json(name = "TotalDeaths")
    val totalDeaths: Int = 0,

    @Json(name = "NewRecovered")
    val newRecovered: Int = 0,

    @Json(name = "TotalRecovered")
    val totalRecovered: Int = 0
)

@JsonClass(generateAdapter = true)
data class CountrySummary(

    @Json(name = "Country")
    val countryName: String = "",

    @Json(name = "CountryCode")
    val countryCode: String = "",

    @Json(name = "Slug")
    val slug: String = "",

    @Json(name = "NewConfirmed")
    val newConfirmed: Int = 0,

    @Json(name = "TotalConfirmed")
    val totalConfirmed: Int = 0,

    @Json(name = "NewDeaths")
    val newDeaths: Int = 0,

    @Json(name = "TotalDeaths")
    val totalDeaths: Int = 0,

    @Json(name = "NewRecovered")
    val newRecovered: Int = 0,

    @Json(name = "TotalRecovered")
    val totalRecovered: Int = 0,

    @Json(name = "Date")
    val date: String = ""
)


//@JsonClass(generateAdapter = true)
//data class Country(
//    @Json(name = "Country")
//    val countryName: String = "",
//
//    @Json(name = "Slug")
//    val slug: String = "",
//
//    @Json(name = "ISO2")
//    val iso2: String = ""
//)

/**
 * functions to convert Network results to domain models
 * */
//fun List<Country>.asCountryDomainModel(): List<DomainCountry> {
//    return map{
//        DomainCountry(
//            countryName = it.countryName,
//            slug = it.slug
////            iso2 = it.iso2
//        )}
//}

fun SummaryContainer.asCountriesSummaryDomainModel(): List<DomainCountrySummary> {
    val array = Countries.map {
        DomainCountrySummary(
            countryName = it.countryName,
            slug = it.slug,
            countryCode = it.countryCode,
            date = it.date,
            newConfirmed = it.newConfirmed,
            totalConfirmed = it.totalConfirmed,
            newDeaths = it.newDeaths,
            totalDeaths = it.totalDeaths,
            newRecovered = it.newRecovered,
            totalRecovered = it.totalRecovered
        )
    }.toMutableList()
    val global = DomainCountrySummary(
        countryName = "global",
        slug = "global",
        countryCode = "global",
        date = Date,
        newConfirmed = Global.newConfirmed,
        totalConfirmed = Global.totalConfirmed,
        newRecovered = Global.newRecovered,
        totalRecovered = Global.totalRecovered,
        newDeaths = Global.newDeaths,
        totalDeaths = Global.totalDeaths
    )
    array.add(0, global)
    return array.toList()
}

//fun SummaryContainer.asGlobalSummaryDomainModel(): DomainGlobalSummary {
//    return DomainGlobalSummary(
//        newConfirmed = global.newConfirmed,
//        totalConfirmed = global.totalConfirmed,
//        newRecovered = global.newRecovered,
//        totalRecovered = global.totalRecovered,
//        newDeaths = global.newDeaths,
//        totalDeaths = global.totalDeaths
//    )
//}

/**
 * funtions to Convert Network results to database objects
 */

//fun List<Country>.asDatabaseModel(): Array<DatabaseCountry> {
//    val array =  map {
//        DatabaseCountry(
//            countryName = it.countryName,
//            slug = it.slug
//            //iso2 = it.iso2
//        )
//    }.toMutableList()
//    val global = DatabaseCountry(
//        countryName = "Global",
//        slug = "global"
//        //iso2 = "global"
//    )
//    array.add(0, global)
//    return array.toTypedArray()
//}

fun SummaryContainer.asCountriesSummaryDatabaseModel(): Array<DatabaseCountrySummary> {
    val array = Countries.map {
        DatabaseCountrySummary(
            countryName = it.countryName,
            slug = it.slug,
            countryCode = it.countryCode,
            date = it.date,
            newConfirmed = it.newConfirmed,
            totalConfirmed = it.totalConfirmed,
            newDeaths = it.newDeaths,
            totalDeaths = it.totalDeaths,
            newRecovered = it.newRecovered,
            totalRecovered = it.totalRecovered
        )
    }.toMutableList()
    val global = DatabaseCountrySummary(
        countryName = "Global",
        slug = "global",
        countryCode = "global",
        date = Date,
        newConfirmed = Global.newConfirmed,
        totalConfirmed = Global.totalConfirmed,
        newRecovered = Global.newRecovered,
        totalRecovered = Global.totalRecovered,
        newDeaths = Global.newDeaths,
        totalDeaths = Global.totalDeaths
    )
    array.add(0, global)
    return array.toTypedArray()
}

fun SummaryContainer.asDatabaseCountryModel(): Array<DatabaseCountry> {
    val array = Countries.map {
        DatabaseCountry(
            countryName = it.countryName,
            slug = it.slug
        )
    }.toMutableList()
    val global = DatabaseCountry(
        countryName = "Global",
        slug = "global"
    )
    array.add(0, global)
    return array.toTypedArray()
}
//fun SummaryContainer.asGlobalSummaryDatabaseModel(): Array<DatabaseGlobalSummary> {
//    return arrayOf(
//        DatabaseGlobalSummary(
//            slug = "global",
//            newConfirmed = global.newConfirmed,
//            totalConfirmed = global.totalConfirmed,
//            newRecovered = global.newRecovered,
//            totalRecovered = global.totalRecovered,
//            newDeaths = global.totalDeaths,
//            totalDeaths = global.totalDeaths
//        )
//    )
//}