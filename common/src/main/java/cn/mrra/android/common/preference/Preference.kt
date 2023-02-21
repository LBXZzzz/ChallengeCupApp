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

        DayNightDelegate.setDefaultNightMode(
            if (context.isDarkMode) {
                DayNightDelegate.MODE_NIGHT_YES
            } else {
                DayNightDelegate.MODE_NIGHT_NO
            }
        )
        LocaleDelegate.defaultLocale = Locale.getDefault()
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
        if (appDarkModePolicy != userPreference.darkModePolicy) {
            when (userPreference.darkModePolicy) {
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
            appDarkModePolicy = userPreference.darkModePolicy
        }
        if (appLocalePolicy != userPreference.localePolicy) {
            when (userPreference.localePolicy) {
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
            appLocalePolicy = userPreference.localePolicy
        }
    }

    lateinit var appDarkModeDelegate: DayNightDelegate
        private set

    lateinit var appLocaleDelegate: LocaleDelegate
        private set

    /**
     * 深色模式策略，0 代表浅色模式，1 代表深色模式，-1 代表跟随系统
     * */
    var appDarkModePolicy: Int = -1

    /**
     * 语言策略，0 代表简体中文，1 代表英文，-1 代表跟随系统
     * */
    var appLocalePolicy: Int = -1

}