package com.example.paymentmonitor.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paymentmonitor.data.models.PaymentRecord
import com.example.paymentmonitor.data.models.PaymentSource
import com.example.paymentmonitor.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val paymentRecords: List<PaymentRecord> = emptyList(),
    val totalAmount: Double = 0.0,
    val alipayAmount: Double = 0.0,
    val wechatAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val selectedFilter: PaymentFilter = PaymentFilter.ALL,
    val hasNotificationPermission: Boolean = false
)

enum class PaymentFilter {
    ALL, ALIPAY, WECHAT
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    init {
        loadPaymentData()
    }
    
    private fun loadPaymentData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // 组合多个数据流
            combine(
                paymentRepository.getAllPaymentRecords(),
                // 这里可以添加更多的数据流
            ) { allRecords ->
                allRecords
            }.collect { records ->
                updateUiState(records)
            }
        }
    }
    
    private suspend fun updateUiState(records: List<PaymentRecord>) {
        val totalAmount = paymentRepository.getTotalAmount()
        val alipayAmount = paymentRepository.getTotalAmountBySource(PaymentSource.ALIPAY)
        val wechatAmount = paymentRepository.getTotalAmountBySource(PaymentSource.WECHAT)
        
        val currentFilter = _uiState.value.selectedFilter
        val filteredRecords = when (currentFilter) {
            PaymentFilter.ALL -> records
            PaymentFilter.ALIPAY -> records.filter { it.source == PaymentSource.ALIPAY }
            PaymentFilter.WECHAT -> records.filter { it.source == PaymentSource.WECHAT }
        }
        
        _uiState.value = _uiState.value.copy(
            paymentRecords = filteredRecords,
            totalAmount = totalAmount,
            alipayAmount = alipayAmount,
            wechatAmount = wechatAmount,
            isLoading = false
        )
    }
    
    fun setFilter(filter: PaymentFilter) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(selectedFilter = filter)
            
            // 重新过滤记录
            val allRecords = paymentRepository.getAllPaymentRecords()
            allRecords.collect { records ->
                val filteredRecords = when (filter) {
                    PaymentFilter.ALL -> records
                    PaymentFilter.ALIPAY -> records.filter { it.source == PaymentSource.ALIPAY }
                    PaymentFilter.WECHAT -> records.filter { it.source == PaymentSource.WECHAT }
                }
                
                _uiState.value = _uiState.value.copy(
                    paymentRecords = filteredRecords,
                    selectedFilter = filter
                )
            }
        }
    }
    
    fun refreshData() {
        loadPaymentData()
    }
    
    fun updateNotificationPermissionStatus(hasPermission: Boolean) {
        _uiState.value = _uiState.value.copy(hasNotificationPermission = hasPermission)
    }
    
    fun clearAllRecords() {
        viewModelScope.launch {
            paymentRepository.deleteAllPaymentRecords()
            refreshData()
        }
    }
}