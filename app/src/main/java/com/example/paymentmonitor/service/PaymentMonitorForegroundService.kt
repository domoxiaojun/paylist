package com.example.paymentmonitor.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.paymentmonitor.MainActivity
import com.example.paymentmonitor.R

class PaymentMonitorForegroundService : Service() {
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "payment_monitor_service"
        private const val CHANNEL_NAME = "收款监听服务"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // 服务被系统杀死后会尝试重启
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW // 低重要性，不会发出声音或震动
            ).apply {
                description = "收款监听服务运行状态"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        // 点击通知打开主界面
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.foreground_service_title))
            .setContentText(getString(R.string.foreground_service_message))
            .setSmallIcon(R.drawable.ic_notification) // 需要创建这个图标
            .setContentIntent(pendingIntent)
            .setOngoing(true) // 持续通知，不可被用户滑动删除
            .setAutoCancel(false)
            .build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}