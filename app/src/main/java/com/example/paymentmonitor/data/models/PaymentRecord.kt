package com.example.paymentmonitor.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.parcelize.TypeParceler
import kotlinx.parcelize.Parceler
import android.os.Parcel

@Entity(tableName = "payment_records")
@Parcelize
data class PaymentRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val source: PaymentSource,
    @TypeParceler<Instant, InstantParceler>
    val timestamp: Instant,
    val title: String,
    val description: String,
    val originalNotificationKey: String? = null
) : Parcelable

object InstantParceler : Parceler<Instant> {
    override fun create(parcel: Parcel): Instant {
        return Instant.fromEpochMilliseconds(parcel.readLong())
    }

    override fun Instant.write(parcel: Parcel, flags: Int) {
        toEpochMilliseconds().let { parcel.writeLong(it) }
    }
}

enum class PaymentSource {
    ALIPAY,    // 支付宝
    WECHAT     // 微信
}