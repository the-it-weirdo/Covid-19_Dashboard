package dev.kingominho.covid_19dashboard.domain

data class DomainCountry(
    val countryName: String,
    val slug: String
//    val iso2: String
)

data class DomainCountrySummary(
    val countryName: String,
    val countryCode: String,
    val slug: String,
    val newConfirmed: Int,
    val totalConfirmed: Int,
    val newDeaths: Int,
    val totalDeaths: Int,
    val newRecovered: Int,
    val totalRecovered: Int,
    val date: String
)

//data class DomainGlobalSummary(
//    val newConfirmed: Int,
//    val totalConfirmed: Int,
//    val newDeaths: Int,
//    val totalDeaths: Int,
//    val newRecovered: Int,
//    val totalRecovered: Int
//)

//fun DomainGlobalSummary.asDomainCountrySummaryModel() : DomainCountrySummary {
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
