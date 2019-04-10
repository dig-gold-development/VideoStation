package com.site.vs.videostation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * 单位工具类
 *
 * @author chznzhen
 */
object UnitUtils {

    /**
     * 获取随机用户名
     *
     * @return
     */
    // 输出字母还是数字
    // 字符串
    // 取得大写字母还是小写字母
    // 数字
    val randomAccount: String
        get() {
            var numValue = ""
            val random = Random()
            for (i in 0..9) {
                val charOrNum = if (random.nextInt(2) % 2 == 0) "char" else "num"
                if ("char".equals(charOrNum, ignoreCase = true)) {
                    val choice = if (random.nextInt(2) % 2 == 0) 65 else 97
                    numValue += (choice + random.nextInt(26)).toChar()
                } else if ("num".equals(charOrNum, ignoreCase = true)) {
                    numValue += random.nextInt(10).toString()
                }
            }
            return numValue.toLowerCase()
        }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context?, dpValue: Float): Int {
        var value = dpValue.toInt()
        if (null != context) {
            val scale = context.resources.displayMetrics.density
            value = (dpValue * scale + 0.5f).toInt()
        }
        return value
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context?, pxValue: Float): Int {
        var value = pxValue.toInt()
        if (null != context) {
            val scale = context.resources.displayMetrics.density
            value = (pxValue / scale + 0.5f).toInt()
        }
        return value
    }

    /**
     * get imei 20140521 gai
     *
     * @return
     */
    fun getIMEI(context: Context): String? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (PackageManager.PERMISSION_GRANTED == context.packageManager.checkPermission(
                        Manifest.permission.READ_PHONE_STATE, context.packageName)) {
            telephonyManager.deviceId
        } else {
            null
        }
    }

    fun getWidth(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    fun getHight(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.heightPixels
    }


    fun timeStamp2Date(seconds: String?, format: String?): String {
        var format = format
        if (seconds == null || seconds.isEmpty() || seconds == "null") {
            return ""
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(java.lang.Long.valueOf(seconds + "000")))
    }

    fun getVersionName(context: Context): String {
        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            return ""
        }

    }

    fun secToTime(time: Int): String {
        var time = time
        var timeStr: String? = null
        var hour = 0
        var minute = 0
        var second = 0
        if (time <= 0)
            return "00:00"
        else {
            time /= 1000
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99)
                    return "99:59:59"
                minute %= 60
                second = time - hour * 3600 - minute * 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    fun unitFormat(i: Int): String {
        var retStr: String? = null
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i)
        else
            retStr = "" + i
        return retStr
    }
}
