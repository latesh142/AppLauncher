package com.example.applauncherlist

import android.graphics.drawable.Drawable

interface InstallationListener {
    fun installApp(packageName: String)
    fun unInstallApp(packageName: String)
}