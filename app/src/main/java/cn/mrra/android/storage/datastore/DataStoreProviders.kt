package cn.mrra.android.storage.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cn.mrra.android.common.preference.UserPreference
import kotlinx.coroutines.flow.first

private val Context.preference by preferencesDataStore("preference")

private val darkModeKey = intPreferencesKey("darkMode")

private val localeKey = intPreferencesKey("locale")

suspend fun Context.loadPreference(): UserPreference {
    return preference.data.first().let {
        UserPreference(
            darkModePolicy = it[darkModeKey] ?: -1,
            localePolicy = it[localeKey] ?: -1
        )
    }
}

suspend fun Context.savePreference(userPreference: UserPreference) {
    preference.edit {
        it[darkModeKey] = userPreference.darkModePolicy
        it[localeKey] = userPreference.localePolicy
    }
}
