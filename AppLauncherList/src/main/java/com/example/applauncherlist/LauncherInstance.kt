package com.example.applauncherlist

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

object LauncherInstance {

    fun getApplications(packageManager: PackageManager): ArrayList<AppInfo> {
        val appList = ArrayList<AppInfo>()
        val packageInfos = packageManager.getInstalledPackages(0)
        for (packageInfo in packageInfos) {
            Log.d("package_name_list", "packageName" + packageInfo.packageName)
            Log.d(
                "package_name_list",
                "appname" + packageManager.getApplicationLabel(packageInfo.applicationInfo)
            )
            Log.d(
                "package_name_list",
                "launch" + packageManager.getLaunchIntentForPackage(packageInfo.packageName)
            )
            Log.d("package_name_list", "version_name" + packageInfo.versionName)
            Log.d("package_name_list", "version_code" + packageInfo.versionCode)
            val appInfo = AppInfo()
            appInfo.packageName = packageInfo.packageName
            appInfo.appName = "" + packageManager.getApplicationLabel(packageInfo.applicationInfo)
            appInfo.launchIntent = packageManager.getLaunchIntentForPackage(packageInfo.packageName)
            appInfo.icon = packageManager.getApplicationIcon(packageInfo.packageName)
            appInfo.versionName = packageInfo.versionName
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appInfo.versionCode = packageInfo.longVersionCode
            } else appInfo.versionCode = packageInfo.versionCode.toLong()

            appList.add(appInfo)
        }

        appList.sortWith(Comparator { o1, o2 -> o1.appName.compareTo(o2.appName) })

        return appList
    }

    fun setInstallationReceiver(
        context: Context, installationListener: InstallationListener
    ) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        intentFilter.addDataScheme("package")
        context.registerReceiver(object : AppChangeReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val packageName = "" + p1!!.data.toString().replace("package:", "")

                if (p1!!.action == Intent.ACTION_PACKAGE_ADDED ||
                    p1!!.action == Intent.ACTION_PACKAGE_INSTALL
                ) {
                    installationListener.installApp(packageName)
                } else if (p1!!.action == Intent.ACTION_PACKAGE_REMOVED) {
                    installationListener.unInstallApp(packageName)
                }
            }
        }, intentFilter)
    }
}