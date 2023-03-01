package cn.mrra.android.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.databinding.ActivityConsultChatBinding

class ConsultChatActivity : SimpleActivity<ActivityConsultChatBinding>() {

    override val layoutId: Int = R.layout.activity_consult_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.ivConsultChatReturn.setOnClickListener{finish()}
    }
}