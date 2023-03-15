package cn.mrra.android.ui.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.startActivity
import cn.mrra.android.databinding.FragmentHealthBinding
import cn.mrra.android.ui.activity.ConsultActivity
import cn.mrra.android.ui.activity.FeedbackActivity


class HealthFragment : SimpleFragment<FragmentHealthBinding>() {

    override val layoutId: Int = R.layout.fragment_health

    override fun onFragmentCreated(savedInstanceState: Bundle?) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = requireActivity().window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }


        binding.cvHealth1.setOnClickListener {
            startActivity<ConsultActivity>()
        }
        binding.cvHealth2.setOnClickListener {
            startActivity<FeedbackActivity>()
        }
    }


}