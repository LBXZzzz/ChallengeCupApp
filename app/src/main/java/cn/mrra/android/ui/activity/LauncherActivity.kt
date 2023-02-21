package cn.mrra.android.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.common.preference.Preference
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.ActivityLauncherBinding
import cn.mrra.android.storage.datastore.loadPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LauncherActivity : SimpleActivity<ActivityLauncherBinding>() {

    override val layoutId: Int = R.layout.activity_launcher

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val userPreference = withContext(Dispatchers.IO) {
                loadPreference()
            }
            Preference.onNewUserPreference(userPreference)
            startActivity<MRRAActivity>()
            finish()
        }
    }
}