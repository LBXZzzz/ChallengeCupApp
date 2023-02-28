package cn.mrra.android.ui.fragment

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.FragmentHealthBinding
import cn.mrra.android.ui.activity.ConsultActivity

class HealthFragment : SimpleFragment<FragmentHealthBinding>() {

    override val layoutId: Int = R.layout.fragment_health

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        binding.ivHealthInquiry.setOnClickListener {
            startActivity<ConsultActivity>()
        }
    }

}