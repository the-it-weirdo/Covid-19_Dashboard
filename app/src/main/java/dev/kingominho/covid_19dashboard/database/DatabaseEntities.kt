package dev.kingominho.covid_19dashboard.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.kingominho.covid_19dashboard.domain.DomainCountry
import dev.kingominho.covid_19dashboard.domain.DomainCountrySummary
import dev.kingominho.covid_19dashboard.util.convertDateFormat

//@Entity(tableName = "database_country")
//data class DatabaseCountry constructor(
//    @PrimaryKey
//    val slug: String,
//    val countryName: String,
//    val iso2: String
//)

@Entity(tableName = "summary_table")
data class DatabaseCountrySummary constructor(
    @PrimaryKey
    val slug: String,
    val countryName: String,
    val countryCode: String,
    val newConfirmed: Int,
    val totalConfirmed: Int,
    val newDeaths: Int,
    val totalDeaths: Int,
    val newRecovered: Int,
    val totalRecovered: Int,
    val date: String
)

data class DatabaseCountry constructor(
    val countryName: String,
    val slug: String
)

/**
 * Keeping global separate instead of merging with country summaries for any possible future updates
 */
//@Entity
//data class DatabaseGlobalSummary(
//    @PrimaryKey
//    val slug: String,
//    val newConfirmed: Int,
//    val totalConfirmed: Int,
//    val newDeaths: Int,
//    val totalDeaths: Int,
//    val newRecovered: Int,
//    val totalRecovered: Int
//)

/**
 * Functions to convert Database objects to domain models
 */

//fun List<DatabaseCountry>.asCountryDomainModel(): List<DomainCountry> {
//    return map {
//        DomainCountry(
//            countryName = it.countryName,
//            slug = it.slug,
//            iso2 = it.iso2
//        )
//    }
//}

fun List<DatabaseCountrySummary>.asCountriesSummaryDomainModel(): List<DomainCountrySummary> {
    return map {
        DomainCountrySummary(
            countryName = it.countryName,
            slug = it.slug,
            countryCode = it.countryCode,
            newConfirmed = it.newConfirmed,
            totalConfirmed = it.totalConfirmed,
            newRecovered = it.newRecovered,
            totalRecovered = it.totalRecovered,
            newDeaths = it.newDeaths,
            totalDeaths = it.totalDeaths,
            date = it.date.convertDateFormat()
        )
    }
}



fun DatabaseCountrySummary.asDomainCountrySummary(): DomainCountrySummary {
    return DomainCountrySummary(
        countryName = countryName,
        slug = slug,
        countryCode = countryCode,
        newConfirmed = newConfirmed,
        totalConfirmed = totalConfirmed,
        newRecovered = newRecovered,
        totalRecovered = totalRecovered,
        newDeaths = newDeaths,
        totalDeaths = totalDeaths,
        date = date.convertDateFormat()
    )
}

fun List<DatabaseCountry>.asDomainCountryModel() : List<DomainCountry> {
    return map {
        DomainCountry(
            countryName = it.countryName,
            slug = it.slug
        )
    }
}

//fun List<DatabaseGlobalSummary>.asGlobalSummaryDomainModel(): List<DomainGlobalSummary> {
//    return map {
//        DomainGlobalSummary(
//            newConfirmed = it.newConfirmed,
//            totalConfirmed = it.totalConfirmed,
//            newRecovered = it.newRecovered,
//            totalRecovered = it.totalRecovered,
//            newDeaths = it.newDeaths,
//            totalDeaths = it.totalDeaths
//        )
//    }
//}
//
//fun List<DatabaseGlobalSummary>.asCountrySummaryModel(): List<DomainCountrySummary> {
//    return map {
//        DomainCountrySummary(
//            countryName = "Global",
//            slug = "global",
//            countryCode = "global",
//            newConfirmed = it.newConfirmed,
//            totalConfirmed = it.totalConfirmed,
//            newRecovered = it.newRecovered,
//            totalRecovered = it.totalRecovered,
//            newDeaths = it.newDeaths,
//            totalDeaths = it.totalDeaths,
//            date = ""
//        )
//    }
//}
//
//fun DatabaseGlobalSummary.asDomainCountrySummaryModel() : DomainCountrySummary {
//    return DomainCountrySummary(
//        countryName = "Global",
//        slug = "global",
//        countryCode = "global",
//        newConfirmed = newConfirmed,
//        totalConfirmed = totalConfirmed,
//        newRecovered = newRecovered,
//        totalRecovered = totalRecovered,
//        newDeaths = newDeaths,
//        totalDeaths = totalDeaths,
//        date = ""
//    )
//}