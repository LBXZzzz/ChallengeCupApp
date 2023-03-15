package cn.mrra.android.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleActivity
import cn.mrra.android.databinding.ActivityConsultChatBinding
import cn.mrra.android.databinding.ActivityFeedbackBinding

class FeedbackActivity : SimpleActivity<ActivityFeedbackBinding>() {

    override val layoutId: Int = R.layout.activity_feedback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //通知栏沉浸式
        window.statusBarColor = Color.TRANSPARENT
        val lly = findViewById<ScrollView>(R.id.sv)
        lly.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}