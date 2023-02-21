package cn.mrra.android.mrra

import android.content.res.Configuration
import cn.mrra.android.common.base.BaseApplication
import cn.mrra.android.common.base.appContext
import cn.mrra.android.common.preference.Preference

class MRRAApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Preference.init(appContext)
        MRRA.init(appContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Preference.onNewConfiguration(newConfig)
    }

}