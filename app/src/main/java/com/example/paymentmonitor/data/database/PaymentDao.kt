package com.example.paymentmonitor.data.database

import androidx.room.*
import com.example.paymentmonitor.data.models.PaymentRecord
import com.example.paymentmonitor.data.models.PaymentSource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface PaymentDao {
    
    @Query("SELECT * FROM payment_records ORDER BY timestamp DESC")
    fun getAllPaymentRecords(): Flow<List<PaymentRecord>>
    
    @Query("SELECT * FROM payment_records WHERE source = :source ORDER BY timestamp DESC")
    fun getPaymentRecordsBySource(source: PaymentSource): Flow<List<PaymentRecord>>
    
    @Query("SELECT * FROM payment_records WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getPaymentRecordsByTimeRange(startTime: Instant, endTime: Instant): Flow<List<PaymentRecord>>
    
    @Query("SELECT SUM(amount) FROM payment_records")
    suspend fun getTotalAmount(): Double?
    
    @Query("SELECT SUM(amount) FROM payment_records WHERE source = :source")
    suspend fun getTotalAmountBySource(source: PaymentSource): Double?
    
    @Query("SELECT SUM(amount) FROM payment_records WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getTotalAmountByTimeRange(startTime: Instant, endTime: Instant): Double?
    
    @Query("SELECT * FROM payment_records WHERE id = :id")
    suspend fun getPaymentRecordById(id: Long): PaymentRecord?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentRecord(record: PaymentRecord): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentRecords(records: List<PaymentRecord>)
    
    @Update
    suspend fun updatePaymentRecord(record: PaymentRecord)
    
    @Delete
    suspend fun deletePaymentRecord(record: PaymentRecord)
    
    @Query("DELETE FROM payment_records")
    suspend fun deleteAllPaymentRecords()
    
    @Query("DELETE FROM payment_records WHERE timestamp < :cutoffTime")
    suspend fun deleteOldPaymentRecords(cutoffTime: Instant)
    
    // 分页查询
    @Query("SELECT * FROM payment_records ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getPaymentRecordsPaged(limit: Int, offset: Int): List<PaymentRecord>
    
    @Query("SELECT COUNT(*) FROM payment_records")
    suspend fun getPaymentRecordsCount(): Int
}