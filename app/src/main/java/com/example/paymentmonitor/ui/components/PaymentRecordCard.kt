package com.example.paymentmonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.paymentmonitor.R
import com.example.paymentmonitor.data.models.PaymentRecord
import com.example.paymentmonitor.data.models.PaymentSource
import com.example.paymentmonitor.ui.theme.AlipayBlue
import com.example.paymentmonitor.ui.theme.WeChatGreen
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentRecordCard(
    paymentRecord: PaymentRecord,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sourceColor = when (paymentRecord.source) {
        PaymentSource.ALIPAY -> AlipayBlue
        PaymentSource.WECHAT -> WeChatGreen
    }
    
    val sourceIcon = when (paymentRecord.source) {
        PaymentSource.ALIPAY -> Icons.Default.AccountBalanceWallet
        PaymentSource.WECHAT -> Icons.Default.CreditCard
    }
    
    val sourceText = when (paymentRecord.source) {
        PaymentSource.ALIPAY -> stringResource(R.string.payment_source_alipay)
        PaymentSource.WECHAT -> stringResource(R.string.payment_source_wechat)
    }
    
    val formatter = DecimalFormat("0.00")
    val localDateTime = paymentRecord.timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
    val timeText = "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 支付来源图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(sourceColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = sourceIcon,
                    contentDescription = sourceText,
                    tint = sourceColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 收款信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = paymentRecord.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = sourceText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (paymentRecord.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = paymentRecord.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 金额和时间
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${stringResource(R.string.currency_symbol)}${formatter.format(paymentRecord.amount)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}