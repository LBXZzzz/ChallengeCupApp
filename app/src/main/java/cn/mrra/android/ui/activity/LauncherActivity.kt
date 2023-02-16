package cn.mrra.android.ui.activity

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.ActivityLauncherBinding

class LauncherActivity : SimpleActivity<ActivityLauncherBinding>() {

    override val layoutId: Int = R.layout.activity_launcher

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        startActivity<MRRAActivity>()
    }
}