package cn.mrra.android.ui.fragment.control

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.databinding.FragmentInitiativeBinding
import cn.mrra.android.mrra.MRRA

class InitiativeFragment : SimpleFragment<FragmentInitiativeBinding>() {

    override val layoutId: Int = R.layout.fragment_initiative

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding) {
            tvInitSend.setOnClickListener {
                MRRA.INSTANCE.writeInitiative()
            }
        }
    }

}