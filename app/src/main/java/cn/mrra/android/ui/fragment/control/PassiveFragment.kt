package cn.mrra.android.ui.fragment.control

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.FragmentPassiveBinding
import cn.mrra.android.mrra.MRRA
import com.google.android.material.textview.MaterialTextView

class PassiveFragment : SimpleFragment<FragmentPassiveBinding>() {

    override val layoutId: Int = R.layout.fragment_passive

    private var passiveMode = 0

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding) {
            tvPassive1.setOnClickListener {
                selectPassiveMode(1)
            }

            tvPassive2.setOnClickListener {
                selectPassiveMode(2)
            }

            tvPassive3.setOnClickListener {
                selectPassiveMode(3)
            }

            tvPassive4.setOnClickListener {
                selectPassiveMode(4)
            }

            tvPassive5.setOnClickListener {
                selectPassiveMode(5)
            }

            tvPassiveSend.setOnClickListener {
                if (passiveMode != 0) {
                    MRRA.INSTANCE.writePassive(passiveMode)
                } else {
                    toastMsg(getString(R.string.mrra_control_passive_mode_select_first))
                }
            }
        }
    }

    private fun selectPassiveMode(mode: Int) {
        passiveMode = mode
        toastMsg("${getString(R.string.mrra_control_passive_mode_select)}: $mode")
    }

}