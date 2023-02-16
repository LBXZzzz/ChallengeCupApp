package cn.mrra.android.ui.activity

import android.os.Bundle
import cn.mrra.android.common.base.BaseActivity
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.ActivityLauncherBinding

class LauncherActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        startActivity<MRRAActivity>()
    }
}