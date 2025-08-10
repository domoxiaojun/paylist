package com.example.paymentmonitor.di

import android.content.Context
import androidx.room.Room
import com.example.paymentmonitor.data.database.PaymentDao
import com.example.paymentmonitor.data.database.PaymentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun providePaymentDatabase(@ApplicationContext context: Context): PaymentDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PaymentDatabase::class.java,
            "payment_database"
        ).build()
    }
    
    @Provides
    fun providePaymentDao(database: PaymentDatabase): PaymentDao {
        return database.paymentDao()
    }
}