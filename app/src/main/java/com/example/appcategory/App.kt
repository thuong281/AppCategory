package com.example.appcategory

data class App(
    val appName: String = "",
    val appPkgName: String = "",
    val appDescription: String = "",
    var appVersionName: String = "",
    val appVersionCode: Int = 0,
    var isInstalled: Boolean = false,
    var hasUpdate: Boolean = false,
    val downloadUrl: String = "https://download-app.ghtk.vn/dl/taixe/android",
    var downloadPercent: Int = 0
)