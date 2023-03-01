package cn.mrra.android.ui.activity

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.ActivityConsultBinding

class ConsultActivity : SimpleActivity<ActivityConsultBinding>() {

    override val layoutId: Int = R.layout.activity_consult

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.tooBarConsultReturn.setNavigationOnClickListener { finish() }
        binding.btConsultConsult.setOnClickListener{
            startActivity<ConsultChatActivity> {  }
        }
    }
}