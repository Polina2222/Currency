package com.example.myapplication1.LocalDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class, ExchangeHistory::class], version = 2, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun dao(): CurrencyDao

    companion object {
        @Volatile
        var database: CurrencyDatabase? = null

        fun getInstance(context: Context): CurrencyDatabase? {
            if (database == null) {
                synchronized(this) {
                    var db = Room.databaseBuilder(
                        context.applicationContext,
                        CurrencyDatabase::class.java, "currency_database"
                    ).build()
                    database = db
                    return db
                }
            }
            return database
        }
    }
}