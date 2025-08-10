package com.example.paymentmonitor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.paymentmonitor.R
import com.example.paymentmonitor.ui.theme.AlipayBlue
import com.example.paymentmonitor.ui.theme.WeChatGreen
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSummaryCard(
    totalAmount: Double,
    alipayAmount: Double,
    wechatAmount: Double,
    modifier: Modifier = Modifier
) {
    val formatter = DecimalFormat("0.00")
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.total_amount),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${stringResource(R.string.currency_symbol)}${formatter.format(totalAmount)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 支付宝和微信分别统计
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 支付宝统计
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.payment_source_alipay),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AlipayBlue,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${stringResource(R.string.currency_symbol)}${formatter.format(alipayAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AlipayBlue
                    )
                }
                
                // 微信统计
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.payment_source_wechat),
                        style = MaterialTheme.typography.bodyMedium,
                        color = WeChatGreen,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${stringResource(R.string.currency_symbol)}${formatter.format(wechatAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = WeChatGreen
                    )
                }
            }
        }
    }
}