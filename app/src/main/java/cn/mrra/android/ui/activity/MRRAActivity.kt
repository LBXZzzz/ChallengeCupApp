package cn.mrra.android.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import cn.mrra.android.R
import cn.mrra.android.common.base.ACTION_FRAGMENT
import cn.mrra.android.common.base.ACTION_NAVIGATE
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.databinding.ActivityMrraBinding

class MRRAActivity : SimpleActivity<ActivityMrraBinding>() {

    override val layoutId: Int = R.layout.activity_mrra

    private lateinit var navController: NavController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initNav()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null && ((intent.action == ACTION_NAVIGATE) || (intent.action == ACTION_FRAGMENT))) {
            val id = intent.getIntExtra("id", 0)
            navController.navigate(
                id,
                null,
                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(
                        navController.graph.startDestinationId,
                        inclusive = true,
                        saveState = false
                    ).build()
            )
        }
    }

    private fun initNav() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_mrra_host) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navMrraNav, navController)
    }

}