package com.example.paymentmonitor.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.parcelize.RawValue

@Entity(tableName = "payment_records")
@Parcelize
data class PaymentRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val source: PaymentSource,
    @RawValue
    val timestamp: Instant,
    val title: String,
    val description: String,
    val originalNotificationKey: String? = null
) : Parcelable

enum class PaymentSource {
    ALIPAY,    // 支付宝
    WECHAT     // 微信
}