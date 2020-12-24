package com.example.applauncherlist

import android.content.Intent
import android.graphics.drawable.Drawable

class AppInfo {
    var packageName = ""
    var appName = ""
    var launchIntent: Intent? = null
    var icon: Drawable? = null
    var versionName: String? = null
    var versionCode = 0L
}