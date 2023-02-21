package cn.mrra.android.ui.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.common.base.appContext
import cn.mrra.android.common.preference.Preference
import cn.mrra.android.common.startActivity
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.ActivityLauncherBinding
import cn.mrra.android.mrra.MRRA
import cn.mrra.android.storage.datastore.loadPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LauncherActivity : SimpleActivity<ActivityLauncherBinding>() {

    override val layoutId: Int = R.layout.activity_launcher

    @RequiresApi(Build.VERSION_CODES.S)
    private val permissions31 = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )

    private val permissions21 = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            result.map {
                if (!it.value) {
                    toastMsg(getString(R.string.permission_des), this)
                    finish()
                    return@registerForActivityResult
                }
            }
            MRRA.init(appContext)
            startActivity<MRRAActivity>()
            finish()
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val userPreference = withContext(Dispatchers.IO) {
                loadPreference()
            }
            Preference.onNewUserPreference(userPreference)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLauncher.launch(permissions31)
            } else {
                requestPermissionLauncher.launch(permissions21)
            }
        }
    }
}