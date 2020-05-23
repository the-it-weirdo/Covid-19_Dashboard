package dev.kingominho.covid_19dashboard.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

//@Dao
//interface Covid19CountriesDao {
//
//    @Query("SELECT * FROM database_country ORDER BY countryName ASC")
//    fun getCountries(): LiveData<List<DatabaseCountry>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAllCountries(vararg countries: DatabaseCountry)
//}

//@Dao
//interface Covid19GlobalSummaryDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGlobalSummary(vararg globalSummary: DatabaseGlobalSummary)
//
//    @Query("SELECT * FROM DatabaseGlobalSummary ")
//    fun getGlobalSummary(): LiveData<List<DatabaseGlobalSummary>>
//}

@Dao
interface Covid19CountrySummariesDao {

    @Query("SELECT * FROM summary_table WHERE slug = :slug")
    fun getCountrySummary(slug: String): LiveData<DatabaseCountrySummary>

    @Query("SELECT * FROM  summary_table ORDER BY countryName ASC")
    fun getAllSummary(): LiveData<List<DatabaseCountrySummary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCountrySummaries(vararg countrySummaries: DatabaseCountrySummary)

    @Query("SELECT countryName, slug FROM summary_table ORDER BY countryName ASC")
    fun getAllCountries(): LiveData<List<DatabaseCountry>>
}

@Database(
    entities = [
//        DatabaseCountry::class,
//        DatabaseGlobalSummary::class,
        DatabaseCountrySummary::class],
    version = 1,
    exportSchema = false
)
abstract class Covid19RoomDatabase : RoomDatabase() {
    //    abstract val countryDao: Covid19CountriesDao
//  abstract val globalDao: Covid19GlobalSummaryDao
    abstract val summaryDao: Covid19CountrySummariesDao
}

private lateinit var INSTANCE: Covid19RoomDatabase

fun getDatabase(context: Context): Covid19RoomDatabase {
    synchronized(Covid19RoomDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                Covid19RoomDatabase::class.java,
                "covid19SummaryRoomDB"
            ).build()
        }
    }
    return INSTANCE
}