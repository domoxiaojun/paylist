package com.example.paymentmonitor.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.paymentmonitor.data.models.PaymentRecord

@Database(
    entities = [PaymentRecord::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PaymentDatabase : RoomDatabase() {
    
    abstract fun paymentDao(): PaymentDao
    
    companion object {
        @Volatile
        private var INSTANCE: PaymentDatabase? = null
        
        fun getDatabase(context: Context): PaymentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PaymentDatabase::class.java,
                    "payment_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}