package cn.mrra.android.common.preference

import android.content.Context
import android.content.res.Configuration
import cn.mrra.android.common.base.appContext
import cn.mrra.android.common.isDarkMode
import rikka.material.app.DayNightDelegate
import rikka.material.app.LocaleDelegate
import java.util.*

@Suppress("StaticFieldLeak")
object Preference {

    fun init(context: Context) {
        appDarkModeDelegate = DayNightDelegate(context)
        appLocaleDelegate = LocaleDelegate()
    }

    fun onNewConfiguration(newConfig: Configuration) {
        if (appDarkModePolicy == -1) {
            DayNightDelegate.setDefaultNightMode(
                if (newConfig.isDarkMode) {
                    DayNightDelegate.MODE_NIGHT_YES
                } else {
                    DayNightDelegate.MODE_NIGHT_NO
                }
            )
        }
        if (appLocalePolicy == -1) {
            LocaleDelegate.defaultLocale = newConfig.locale
        }
    }

    fun onNewUserPreference(userPreference: UserPreference) {
        if (appDarkModePolicy != userPreference.darkMode) {
            when (userPreference.darkMode) {
                0 -> {
                    DayNightDelegate.setDefaultNightMode(
                        DayNightDelegate.MODE_NIGHT_NO
                    )
                }
                1 -> {
                    DayNightDelegate.setDefaultNightMode(
                        DayNightDelegate.MODE_NIGHT_YES
                    )
                }
                else -> {
                    DayNightDelegate.setDefaultNightMode(
                        if (appContext.isDarkMode) {
                            DayNightDelegate.MODE_NIGHT_YES
                        } else {
                            DayNightDelegate.MODE_NIGHT_NO
                        }
                    )
                }
            }
            appDarkModePolicy = userPreference.darkMode
        }
        if (appLocalePolicy != userPreference.locale) {
            when (userPreference.locale) {
                0 -> {
                    LocaleDelegate.defaultLocale = Locale.CHINA
                }
                1 -> {
                    LocaleDelegate.defaultLocale = Locale.ENGLISH
                }
                else -> {
                    LocaleDelegate.defaultLocale = Locale.getDefault()
                }
            }
            appLocalePolicy = userPreference.locale
        }
    }

    lateinit var appDarkModeDelegate: DayNightDelegate
        private set

    lateinit var appLocaleDelegate: LocaleDelegate
        private set

    var appDarkModePolicy: Int = -1

    var appLocalePolicy: Int = -1

}