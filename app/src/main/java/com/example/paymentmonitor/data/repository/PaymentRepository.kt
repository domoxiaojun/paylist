package com.example.paymentmonitor.data.repository

import com.example.paymentmonitor.data.database.PaymentDao
import com.example.paymentmonitor.data.models.PaymentRecord
import com.example.paymentmonitor.data.models.PaymentSource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val paymentDao: PaymentDao
) {
    
    fun getAllPaymentRecords(): Flow<List<PaymentRecord>> {
        return paymentDao.getAllPaymentRecords()
    }
    
    fun getPaymentRecordsBySource(source: PaymentSource): Flow<List<PaymentRecord>> {
        return paymentDao.getPaymentRecordsBySource(source)
    }
    
    fun getPaymentRecordsByTimeRange(startTime: Instant, endTime: Instant): Flow<List<PaymentRecord>> {
        return paymentDao.getPaymentRecordsByTimeRange(startTime, endTime)
    }
    
    suspend fun getTotalAmount(): Double {
        return paymentDao.getTotalAmount() ?: 0.0
    }
    
    suspend fun getTotalAmountBySource(source: PaymentSource): Double {
        return paymentDao.getTotalAmountBySource(source) ?: 0.0
    }
    
    suspend fun getTotalAmountByTimeRange(startTime: Instant, endTime: Instant): Double {
        return paymentDao.getTotalAmountByTimeRange(startTime, endTime) ?: 0.0
    }
    
    suspend fun getPaymentRecordById(id: Long): PaymentRecord? {
        return paymentDao.getPaymentRecordById(id)
    }
    
    suspend fun insertPaymentRecord(record: PaymentRecord): Long {
        return paymentDao.insertPaymentRecord(record)
    }
    
    suspend fun insertPaymentRecords(records: List<PaymentRecord>) {
        paymentDao.insertPaymentRecords(records)
    }
    
    suspend fun updatePaymentRecord(record: PaymentRecord) {
        paymentDao.updatePaymentRecord(record)
    }
    
    suspend fun deletePaymentRecord(record: PaymentRecord) {
        paymentDao.deletePaymentRecord(record)
    }
    
    suspend fun deleteAllPaymentRecords() {
        paymentDao.deleteAllPaymentRecords()
    }
    
    suspend fun deleteOldPaymentRecords(cutoffTime: Instant) {
        paymentDao.deleteOldPaymentRecords(cutoffTime)
    }
    
    suspend fun getPaymentRecordsPaged(limit: Int, offset: Int): List<PaymentRecord> {
        return paymentDao.getPaymentRecordsPaged(limit, offset)
    }
    
    suspend fun getPaymentRecordsCount(): Int {
        return paymentDao.getPaymentRecordsCount()
    }
}