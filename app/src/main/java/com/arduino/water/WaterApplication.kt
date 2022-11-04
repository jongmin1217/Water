package com.arduino.water

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

class WaterApplication : Application() {

    var houseMember = 1

    //올림픽 규격
    val standard = 2000

    // 1L당 금액
    val waterPrice = 200

    //1인 기준 권장 물 사용량
    val waterConsumption = 100

    companion object {
        lateinit var mInstance: WaterApplication
        fun get(): WaterApplication {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()

        mInstance = this

        setUpTimber()

    }

    private fun setUpTimber(){
        val pm = packageManager
        try {
            val appInfo = pm.getApplicationInfo(packageName, 0)
            if (0 != appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                Timber.plant(Timber.DebugTree())
            } else {
                Timber.plant(CrashReportingTree())
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.plant(CrashReportingTree())
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, @NonNull message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }

}