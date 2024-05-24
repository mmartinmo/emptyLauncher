package com.qq42labs.screenfreezedemo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log

class DefaultLauncherChecker(private val context: Context) {

    private val TAG:String = "DefaultLauncherChecker"

    fun isThisAppDefault(): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            val resolveInfo: ResolveInfo? =
                context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            val currentHomePackage = resolveInfo?.activityInfo?.packageName?:""
            val myPackageName: String = context.packageName
            val result = myPackageName == currentHomePackage
            result
        } catch (e: Exception) {
            true
        }
    }
}