package com.example.appcategory


import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
        private const val NOTIFICATION_ID = 1
    }

    lateinit var downloadController: DownloadController

    lateinit var adapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListApp()
    }

    private fun setListApp() {
        val listInstalledPackage: MutableList<Pair<String, Int>> = ArrayList()
        thread {
            val pm = packageManager
            val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
            for (packageInfo in packages) {
                if (packageInfo.packageName.contains("ghtk")) {
                    listInstalledPackage.add(Pair(packageInfo.packageName, packageInfo.versionCode))
                }
            }
            val listApp: MutableList<App> = ArrayList()
            checkAppInstalled(
                App(
                    appName = "Driver&X",
                    appPkgName = "vn.ghtk.xteam",
                    appDescription = "Dành cho CoD, tài xế GHTK",
                    appVersionCode = 50000,
                    appVersionName = "1.2.3"
                ),
                listInstalledPackage, listApp
            )
            checkAppInstalled(
                App(
                    appName = "GHTK noi bo",
                    appPkgName = "vn.ghtk.noibo",
                    appDescription = "Dành cho nhân viên vận hành và khối văn phòng",
                    appVersionCode = 500,
                    appVersionName = "1.2.31",
                    downloadUrl = "https://download-app.ghtk.vn/dl/noibo/android"
                ),
                listInstalledPackage, listApp
            )
            checkAppInstalled(
                App(
                    appName = "GChats",
                    appPkgName = "gchat.ghtk.vn",
                    appDescription = "Chats, chấm công, News",
                    appVersionCode = 10000,
                    appVersionName = "1.2.311",
                    downloadUrl = "https://download-app.ghtk.vn/dl/gchat/android"
                ),
                listInstalledPackage, listApp
            )
            adapter = AppAdapter()
            adapter.setList(listApp)
            findViewById<RecyclerView>(R.id.rv).adapter = adapter
            adapter.updateApp = { app, position ->
//                val notifyIntent = Intent(this, MainActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                val notifyPendingIntent = PendingIntent.getActivity(
//                    this, 0, notifyIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                )
//                val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
//                    .setContentIntent(notifyPendingIntent)
//                    .setContentTitle("${app.appName} has update!")
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .build()
//                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                notificationManager.notify(NOTIFICATION_ID, notification)
                downloadController = DownloadController(this, app.downloadUrl)
                lifecycleScope.launchWhenCreated {
                    downloadController.enqueueDownload(app).collect {
                        when (it) {
                            is DownloadingState.Downloading -> {
                                app.downloadPercent = it.progress
                                adapter.notifyItemChanged(position, AppAdapter.UPDATE_DOWNLOAD_PERCENT)
                                Log.d("TAG1234", "${app.appName} ${it.progress}")
                            }
                            else -> adapter.notifyItemChanged(position, AppAdapter.DOWNLOAD_FINISH)
                        }
                    }
                }
            }
        }
    }

    private fun checkAppInstalled(
        app: App,
        listInstalledPackage: MutableList<Pair<String, Int>>,
        listApp: MutableList<App>
    ) {
        for (installedPackage in listInstalledPackage) {
            if (TextUtils.equals(app.appPkgName, installedPackage.first)) {
//                app.appVersionName = installedPackage.second
                app.hasUpdate = appNeedUpdate(app.appVersionCode, installedPackage.second)
                app.isInstalled = true
                listApp.add(app)
                return
            }
        }
        app.isInstalled = false
        listApp.add(app)
    }

    private fun appNeedUpdate(installedVersionCode: Int, newVersionCode: Int): Boolean {
        return installedVersionCode > newVersionCode
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloadController.enqueueDownload(App())
            } else {
                // Permission request was denied.
//                mainLayout.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // start downloading
            lifecycleScope.launchWhenCreated {
                downloadController.enqueueDownload(App()).collect {
                    when (it) {
                        is DownloadingState.Downloading -> {
                            Log.d("TAG1234", "${it.progress}")
                        }
                        else -> Unit
                    }
                }
            }
        } else {
            // Permission is missing and must be requested.
        }
    }

}