package cn.mrra.android.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.databinding.ActivityConsultChatBinding

class ConsultChatActivity : SimpleActivity<ActivityConsultChatBinding>() {

    override val layoutId: Int = R.layout.activity_consult_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //通知栏沉浸式
        window.statusBarColor = Color.TRANSPARENT
        val lly = findViewById<LinearLayout>(R.id.lly_consult_chat)
        lly.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        binding.ivConsultChatReturn.setOnClickListener{finish()}
    }
}