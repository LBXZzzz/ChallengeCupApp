package cn.mrra.android.ui.fragment

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.FragmentMeBinding
import cn.mrra.android.ui.activity.SettingsActivity

class MeFragment : SimpleFragment<FragmentMeBinding>() {

    override val layoutId: Int = R.layout.fragment_me

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        binding.tvMeSettings.setOnClickListener {
            startActivity<SettingsActivity>()
        }
    }

}