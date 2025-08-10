package com.example.paymentmonitor.service

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.paymentmonitor.data.models.PaymentRecord
import com.example.paymentmonitor.data.repository.PaymentRepository
import com.example.paymentmonitor.utils.NotificationParser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListenerService : NotificationListenerService() {
    
    @Inject
    lateinit var paymentRepository: PaymentRepository
    
    @Inject
    lateinit var notificationParser: NotificationParser
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    companion object {
        private const val TAG = "NotificationListener"
        
        // 支付应用包名
        const val ALIPAY_PACKAGE = "com.eg.android.AlipayGphone"
        const val WECHAT_PACKAGE = "com.tencent.mm"
        
        // 检查通知监听权限是否已授权
        fun isNotificationAccessGranted(context: Context): Boolean {
            val packageName = context.packageName
            val flat = android.provider.Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            return flat?.contains(packageName) == true
        }
        
        // 跳转到通知访问权限设置页面
        fun requestNotificationAccess(context: Context) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "NotificationListenerService created")
    }
    
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "NotificationListenerService connected")
        
        // 启动前台服务
        startForegroundService()
    }
    
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "NotificationListenerService disconnected")
        
        // 尝试重新连接
        requestRebind(ComponentName(this, NotificationListenerService::class.java))
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        
        sbn?.let { statusBarNotification ->
            handleNotification(statusBarNotification)
        }
    }
    
    private fun handleNotification(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val notification = sbn.notification
        
        // 只处理支付宝和微信的通知
        if (packageName != ALIPAY_PACKAGE && packageName != WECHAT_PACKAGE) {
            return
        }
        
        Log.d(TAG, "Received notification from: $packageName")
        
        // 解析通知内容
        val paymentInfo = notificationParser.parsePaymentNotification(sbn)
        
        if (paymentInfo != null) {
            Log.d(TAG, "Parsed payment info: $paymentInfo")
            
            // 保存收款记录
            serviceScope.launch {
                try {
                    paymentRepository.insertPaymentRecord(paymentInfo)
                    Log.d(TAG, "Payment record saved successfully")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save payment record", e)
                }
            }
        }
    }
    
    private fun startForegroundService() {
        val intent = Intent(this, PaymentMonitorForegroundService::class.java)
        startService(intent)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "NotificationListenerService destroyed")
    }
}