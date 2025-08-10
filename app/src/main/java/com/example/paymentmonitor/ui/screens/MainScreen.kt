package com.example.paymentmonitor.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.paymentmonitor.R
import com.example.paymentmonitor.service.NotificationListenerService
import com.example.paymentmonitor.ui.components.PaymentRecordCard
import com.example.paymentmonitor.ui.components.PaymentSummaryCard
import com.example.paymentmonitor.ui.components.PermissionCard
import com.example.paymentmonitor.ui.viewmodels.MainViewModel
import com.example.paymentmonitor.ui.viewmodels.PaymentFilter
import com.example.paymentmonitor.utils.IntentHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val intentHelper = remember { IntentHelper() }
    
    // 检查通知权限状态
    LaunchedEffect(Unit) {
        val hasPermission = NotificationListenerService.isNotificationAccessGranted(context)
        viewModel.updateNotificationPermissionStatus(hasPermission)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 权限请求卡片
        PermissionCard(
            hasNotificationPermission = uiState.hasNotificationPermission,
            onRequestPermission = {
                NotificationListenerService.requestNotificationAccess(context)
            }
        )
        
        if (!uiState.hasNotificationPermission) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // 统计概览卡片
        PaymentSummaryCard(
            totalAmount = uiState.totalAmount,
            alipayAmount = uiState.alipayAmount,
            wechatAmount = uiState.wechatAmount
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 筛选按钮和刷新按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 筛选按钮
            Row {
                FilterChip(
                    onClick = { viewModel.setFilter(PaymentFilter.ALL) },
                    label = { Text("全部") },
                    selected = uiState.selectedFilter == PaymentFilter.ALL
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                FilterChip(
                    onClick = { viewModel.setFilter(PaymentFilter.ALIPAY) },
                    label = { Text(stringResource(R.string.payment_source_alipay)) },
                    selected = uiState.selectedFilter == PaymentFilter.ALIPAY
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                FilterChip(
                    onClick = { viewModel.setFilter(PaymentFilter.WECHAT) },
                    label = { Text(stringResource(R.string.payment_source_wechat)) },
                    selected = uiState.selectedFilter == PaymentFilter.WECHAT
                )
            }
            
            // 刷新按钮
            IconButton(
                onClick = { viewModel.refreshData() }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "刷新"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 收款记录列表
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.paymentRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_records),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = uiState.paymentRecords,
                    key = { it.id }
                ) { record ->
                    PaymentRecordCard(
                        paymentRecord = record,
                        onClick = {
                            intentHelper.openPaymentApp(context, record.source)
                        }
                    )
                }
            }
        }
    }
}