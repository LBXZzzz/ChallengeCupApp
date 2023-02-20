package cn.mrra.android.mrra

import android.content.res.Configuration
import cn.mrra.android.common.base.BaseApplication
import cn.mrra.android.common.base.appContext
import cn.mrra.android.common.isDarkMode
import rikka.material.app.DayNightDelegate

class MRRAApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        applyThemeMode()
        MRRA.init(appContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyThemeMode()
    }

    private fun applyThemeMode() {
        if (appContext.isDarkMode) {
            DayNightDelegate.setDefaultNightMode(DayNightDelegate.MODE_NIGHT_YES)
        } else {
            DayNightDelegate.setDefaultNightMode(DayNightDelegate.MODE_NIGHT_NO)
        }
    }

}