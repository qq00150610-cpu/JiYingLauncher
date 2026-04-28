package com.jiying.launcher.util

import android.util.Log
import org.json.JSONObject
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * 腾讯云COS签名工具
 * 用于生成临时下载链接
 */
object CosSignUtil {
    private const val TAG = "CosSignUtil"
    
    // COS配置 - 从安全角度考虑，实际生产环境应该从服务器获取签名
    // 密钥应从环境变量或服务器获取，此处使用占位符
    private var SECRET_ID = BuildConfig.COS_SECRET_ID ?: ""
    private var SECRET_KEY = BuildConfig.COS_SECRET_KEY ?: ""
    private const val BUCKET = "jiying-appstore-1423945248"
    private const val REGION = "ap-beijing"
    private const val HOST = "$BUCKET.cos.$REGION.myqcloud.com"
    
    /**
     * 生成带签名的下载URL
     * @param objectPath 对象路径，如 "apps/amap.apk"
     * @param expireSeconds 签名有效期（秒），默认1小时
     * @return 带签名的完整URL
     */
    fun generateSignedUrl(objectPath: String, expireSeconds: Long = 3600): String {
        try {
            val now = System.currentTimeMillis() / 1000
            val expireTime = now + expireSeconds
            
            // KeyTime
            val keyTime = "$now;$expireTime"
            
            // SignKey = HMAC-SHA1(SecretKey, KeyTime)
            val signKey = hmacSha1(SECRET_KEY, keyTime)
            
            // HttpString
            val httpString = "get\n/$objectPath\n\nhost=$HOST\n"
            
            // StringToSign
            val sha1HttpString = sha1(httpString)
            val stringToSign = "sha1\n$keyTime\n$sha1HttpString\n"
            
            // Signature
            val signature = hmacSha1(signKey, stringToSign)
            
            // 构建签名参数
            val signParam = StringBuilder()
            signParam.append("q-sign-algorithm=sha1")
            signParam.append("&q-ak=$SECRET_ID")
            signParam.append("&q-sign-time=$keyTime")
            signParam.append("&q-key-time=$keyTime")
            signParam.append("&q-header-list=host")
            signParam.append("&q-url-param-list=")
            signParam.append("&q-signature=$signature")
            
            // 完整URL
            val signedUrl = "https://$HOST/$objectPath?$signParam"
            
            Log.d(TAG, "Generated signed URL for: $objectPath")
            return signedUrl
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate signed URL: ${e.message}")
            // 返回原始URL作为fallback
            return "https://$HOST/$objectPath"
        }
    }
    
    /**
     * HMAC-SHA1加密
     */
    private fun hmacSha1(key: String, data: String): String {
        val mac = Mac.getInstance("HmacSHA1")
        val secretKeySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA1")
        mac.init(secretKeySpec)
        val bytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * SHA1哈希
     */
    private fun sha1(data: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val bytes = md.digest(data.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * 为应用生成下载URL
     */
    fun getAppDownloadUrl(appPath: String): String {
        // 如果已经包含签名参数，直接返回
        if (appPath.contains("q-sign-algorithm")) {
            return appPath
        }
        
        // 提取对象路径
        val objectPath = if (appPath.startsWith("http")) {
            // 从完整URL中提取路径
            val uri = java.net.URI(appPath)
            uri.path.trimStart('/')
        } else {
            appPath.trimStart('/')
        }
        
        return generateSignedUrl(objectPath, 86400) // 24小时有效
    }
}
