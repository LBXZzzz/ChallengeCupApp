package cn.mrra.android.ui.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.ActivityMrraBinding

class MRRAActivity : SimpleActivity<ActivityMrraBinding>() {

    override val layoutId: Int = R.layout.activity_mrra

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
            initView()
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissionLauncher.launch(permissions31)
        } else {
            requestPermissionLauncher.launch(permissions21)
        }
    }

    private fun initView() {
        initNav()
    }

    private fun initNav() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_mrra_host) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navMrraNav, navController)
    }
}