package cn.mrra.android.common.base

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper

lateinit var appContext: Context

open class BaseApplication : Application() {

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}