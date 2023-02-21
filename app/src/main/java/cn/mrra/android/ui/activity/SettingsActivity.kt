package cn.mrra.android.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import cn.mrra.android.R
import cn.mrra.android.common.base.BaseActivity
import cn.mrra.android.common.preference.Preference
import cn.mrra.android.common.preference.UserPreference
import cn.mrra.android.databinding.ActivitySettingsBinding
import cn.mrra.android.storage.datastore.savePreference
import kotlinx.coroutines.launch

class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsViewModel>() {

    override val layoutId: Int = R.layout.activity_settings

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        with(binding) {
            tvSettingsLangCn.setOnClickListener {
                viewModel.localePolicy.value = 0
            }

            tvSettingsLangEn.setOnClickListener {
                viewModel.localePolicy.value = 1
            }

            tvSettingsLangFollowSystem.setOnClickListener {
                viewModel.localePolicy.value = -1
            }

            tvSettingsModeDarkMode.setOnClickListener {
                viewModel.darkModePolicy.value = 1
            }

            tvSettingsModeLightMode.setOnClickListener {
                viewModel.darkModePolicy.value = 0
            }

            tvSettingsModeFollowSystem.setOnClickListener {
                viewModel.darkModePolicy.value = -1
            }

            lifecycleScope.launch {
                viewModel.localePolicy.collect { localePolicy ->
                    when (localePolicy) {
                        0 -> {
                            ivSettingsLangCn.visibility = View.VISIBLE
                            ivSettingsLangEn.visibility = View.GONE
                            ivSettingsLangFollowSystem.visibility = View.GONE
                        }
                        1 -> {
                            ivSettingsLangCn.visibility = View.GONE
                            ivSettingsLangEn.visibility = View.VISIBLE
                            ivSettingsLangFollowSystem.visibility = View.GONE
                        }
                        else -> {
                            ivSettingsLangCn.visibility = View.GONE
                            ivSettingsLangEn.visibility = View.GONE
                            ivSettingsLangFollowSystem.visibility = View.VISIBLE
                        }
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.darkModePolicy.collect { darkModePolicy ->
                    when (darkModePolicy) {
                        0 -> {
                            ivSettingsModeDarkMode.visibility = View.GONE
                            ivSettingsModeLightMode.visibility = View.VISIBLE
                            ivSettingsModeFollowSystem.visibility = View.GONE
                        }
                        1 -> {
                            ivSettingsModeDarkMode.visibility = View.VISIBLE
                            ivSettingsModeLightMode.visibility = View.GONE
                            ivSettingsModeFollowSystem.visibility = View.GONE
                        }
                        else -> {
                            ivSettingsModeDarkMode.visibility = View.GONE
                            ivSettingsModeLightMode.visibility = View.GONE
                            ivSettingsModeFollowSystem.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK &&
            (viewModel.originLocalePolicy != viewModel.localePolicy.value ||
                    viewModel.originDarkModePolicy != viewModel.darkModePolicy.value)
        ) {
            AlertDialog.Builder(this)
                .setMessage(resources.getString(R.string.setting_save_msg))
                .setPositiveButton(resources.getString(R.string.setting_save_ok)) { _, _ ->
                    lifecycleScope.launch {
                        val userPreference = UserPreference(
                            viewModel.darkModePolicy.value,
                            viewModel.localePolicy.value
                        )
                        Preference.onNewUserPreference(userPreference)
                        savePreference(userPreference)
                    }
                    finish()
                }
                .setNegativeButton(resources.getString(R.string.setting_save_cancel)) { _, _ ->
                    finish()
                }
                .show()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

}