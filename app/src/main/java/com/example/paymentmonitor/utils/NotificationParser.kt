package com.example.paymentmonitor.utils

import android.app.Notification
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.paymentmonitor.data.models.PaymentRecord
import com.example.paymentmonitor.data.models.PaymentSource
import kotlinx.datetime.Clock
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationParser @Inject constructor() {
    
    companion object {
        private const val TAG = "NotificationParser"
        
        // 支付宝收款通知关键词
        private val ALIPAY_KEYWORDS = listOf(
            "收钱到账",
            "收款到账", 
            "支付宝收钱",
            "转账到账"
        )
        
        // 微信收款通知关键词
        private val WECHAT_KEYWORDS = listOf(
            "微信收款助手",
            "收款到账通知",
            "微信支付收款",
            "收款成功"
        )
        
        // 金额提取正则表达式
        private val AMOUNT_PATTERNS = listOf(
            Pattern.compile("收[到款]([0-9]+\\.?[0-9]*)元"),
            Pattern.compile("到账([0-9]+\\.?[0-9]*)元"),
            Pattern.compile("金额([0-9]+\\.?[0-9]*)元"),
            Pattern.compile("￥([0-9]+\\.?[0-9]*)"),
            Pattern.compile("¥([0-9]+\\.?[0-9]*)")
        )
    }
    
    fun parsePaymentNotification(sbn: StatusBarNotification): PaymentRecord? {
        val packageName = sbn.packageName
        val notification = sbn.notification
        
        // 提取通知文本内容
        val notificationText = extractNotificationText(notification)
        
        Log.d(TAG, "Notification text: $notificationText")
        
        // 根据包名判断支付来源
        val paymentSource = when (packageName) {
            "com.eg.android.AlipayGphone" -> PaymentSource.ALIPAY
            "com.tencent.mm" -> PaymentSource.WECHAT
            else -> return null
        }
        
        // 检查是否为收款通知
        if (!isPaymentNotification(notificationText, paymentSource)) {
            return null
        }
        
        // 提取收款金额
        val amount = extractAmount(notificationText)
        if (amount == null || amount <= 0.0) {
            Log.w(TAG, "Failed to extract valid amount from: $notificationText")
            return null
        }
        
        // 创建收款记录
        return PaymentRecord(
            id = 0, // Room会自动生成ID
            amount = amount,
            source = paymentSource,
            timestamp = Clock.System.now(),
            title = extractTitle(notification) ?: "收款通知",
            description = notificationText,
            originalNotificationKey = sbn.key
        )
    }
    
    private fun extractNotificationText(notification: Notification): String {
        val extras = notification.extras
        val text = StringBuilder()
        
        // 尝试获取通知标题
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)
        if (!title.isNullOrEmpty()) {
            text.append(title)
            text.append(" ")
        }
        
        // 尝试获取通知内容
        val content = extras.getCharSequence(Notification.EXTRA_TEXT)
        if (!content.isNullOrEmpty()) {
            text.append(content)
        }
        
        // 尝试获取大文本内容
        val bigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)
        if (!bigText.isNullOrEmpty()) {
            text.append(" ")
            text.append(bigText)
        }
        
        // 尝试获取子文本
        val subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)
        if (!subText.isNullOrEmpty()) {
            text.append(" ")
            text.append(subText)
        }
        
        return text.toString()
    }
    
    private fun extractTitle(notification: Notification): String? {
        val extras = notification.extras
        return extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
    }
    
    private fun isPaymentNotification(text: String, source: PaymentSource): Boolean {
        val keywords = when (source) {
            PaymentSource.ALIPAY -> ALIPAY_KEYWORDS
            PaymentSource.WECHAT -> WECHAT_KEYWORDS
        }
        
        return keywords.any { keyword ->
            text.contains(keyword, ignoreCase = true)
        }
    }
    
    private fun extractAmount(text: String): Double? {
        for (pattern in AMOUNT_PATTERNS) {
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                val amountStr = matcher.group(1)
                try {
                    return amountStr?.toDouble()
                } catch (e: NumberFormatException) {
                    Log.w(TAG, "Failed to parse amount: $amountStr", e)
                    continue
                }
            }
        }
        
        // 如果标准模式都没有匹配到，尝试更宽泛的数字提取
        val numberPattern = Pattern.compile("([0-9]+\\.?[0-9]*)")
        val matcher = numberPattern.matcher(text)
        val amounts = mutableListOf<Double>()
        
        while (matcher.find()) {
            val numberStr = matcher.group(1)
            try {
                val number = numberStr?.toDouble()
                if (number != null && number > 0.01 && number < 999999.99) {
                    amounts.add(number)
                }
            } catch (e: NumberFormatException) {
                continue
            }
        }
        
        // 返回第一个合理的金额
        return amounts.firstOrNull()
    }
}