package cn.mrra.android.ui.fragment.control

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.databinding.FragmentStopBinding
import cn.mrra.android.mrra.MRRA

class StopFragment : SimpleFragment<FragmentStopBinding>() {

    override val layoutId: Int = R.layout.fragment_stop

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding) {
            tvStopSend.setOnClickListener {
                MRRA.INSTANCE.writeStop()
            }
        }
    }

}