package cn.mrra.android.ui.activity

import cn.mrra.android.common.base.BaseViewModel
import cn.mrra.android.common.preference.Preference
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel : BaseViewModel() {

    val originLocalePolicy = Preference.appLocalePolicy

    val originDarkModePolicy = Preference.appDarkModePolicy

    val localePolicy = MutableStateFlow(originLocalePolicy)

    val darkModePolicy = MutableStateFlow(originDarkModePolicy)

}