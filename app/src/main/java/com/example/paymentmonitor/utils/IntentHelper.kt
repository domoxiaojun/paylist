package com.example.paymentmonitor.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import com.example.paymentmonitor.data.models.PaymentSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntentHelper @Inject constructor() {
    
    companion object {
        private const val TAG = "IntentHelper"
        
        // 支付宝相关
        const val ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone"
        const val ALIPAY_SCHEME = "alipays://"
        const val ALIPAY_MAIN_ACTIVITY = "com.alipay.mobile.quinox.LauncherActivity"
        
        // 微信相关
        const val WECHAT_PACKAGE_NAME = "com.tencent.mm"
        const val WECHAT_SCHEME = "weixin://"
        const val WECHAT_MAIN_ACTIVITY = "com.tencent.mm.ui.LauncherUI"
        
        // 应用商店链接
        const val ALIPAY_STORE_URL = "https://play.google.com/store/apps/details?id=$ALIPAY_PACKAGE_NAME"
        const val WECHAT_STORE_URL = "https://play.google.com/store/apps/details?id=$WECHAT_PACKAGE_NAME"
    }
    
    fun openPaymentApp(context: Context, source: PaymentSource): Boolean {
        return when (source) {
            PaymentSource.ALIPAY -> openAlipayApp(context)
            PaymentSource.WECHAT -> openWeChatApp(context)
        }
    }
    
    private fun openAlipayApp(context: Context): Boolean {
        // 首先尝试使用深度链接打开支付宝收款页面
        try {
            val deepLinkIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("${ALIPAY_SCHEME}platformapi/startapp?appId=20000123") // 收钱码页面
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            if (canHandleIntent(context, deepLinkIntent)) {
                context.startActivity(deepLinkIntent)
                Log.d(TAG, "Opened Alipay via deep link")
                return true
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to open Alipay via deep link", e)
        }
        
        // 尝试打开支付宝主界面
        try {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(ALIPAY_PACKAGE_NAME)
            
            if (intent != null) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
                Log.d(TAG, "Opened Alipay main app")
                return true
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to open Alipay main app", e)
        }
        
        // 如果支付宝未安装，跳转到应用商店
        return openAppInStore(context, ALIPAY_STORE_URL)
    }
    
    private fun openWeChatApp(context: Context): Boolean {
        // 首先尝试使用深度链接打开微信收款页面
        try {
            val deepLinkIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("${WECHAT_SCHEME}dl/business/?ticket=t_pay_code") // 收款码页面
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            if (canHandleIntent(context, deepLinkIntent)) {
                context.startActivity(deepLinkIntent)
                Log.d(TAG, "Opened WeChat via deep link")
                return true
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to open WeChat via deep link", e)
        }
        
        // 尝试打开微信主界面
        try {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(WECHAT_PACKAGE_NAME)
            
            if (intent != null) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
                Log.d(TAG, "Opened WeChat main app")
                return true
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to open WeChat main app", e)
        }
        
        // 如果微信未安装，跳转到应用商店
        return openAppInStore(context, WECHAT_STORE_URL)
    }
    
    private fun openAppInStore(context: Context, storeUrl: String): Boolean {
        return try {
            val storeIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(storeUrl)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(storeIntent)
            Log.d(TAG, "Opened app store: $storeUrl")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open app store", e)
            false
        }
    }
    
    private fun canHandleIntent(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return activities.isNotEmpty()
    }
    
    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    
    fun getAppInstallStatus(context: Context): AppInstallStatus {
        val alipayInstalled = isAppInstalled(context, ALIPAY_PACKAGE_NAME)
        val wechatInstalled = isAppInstalled(context, WECHAT_PACKAGE_NAME)
        
        return AppInstallStatus(
            alipayInstalled = alipayInstalled,
            wechatInstalled = wechatInstalled
        )
    }
}

data class AppInstallStatus(
    val alipayInstalled: Boolean,
    val wechatInstalled: Boolean
)