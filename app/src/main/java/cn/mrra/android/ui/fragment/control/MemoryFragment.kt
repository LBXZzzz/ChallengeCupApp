package cn.mrra.android.ui.fragment.control

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.databinding.FragmentMemoryBinding
import cn.mrra.android.mrra.MRRA

class MemoryFragment : SimpleFragment<FragmentMemoryBinding>() {

    override val layoutId: Int = R.layout.fragment_memory

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        binding.tvMemoryStart.setOnClickListener {
            MRRA.INSTANCE.startToRemember()
        }
        binding.tvMemoryStop.setOnClickListener {
            MRRA.INSTANCE.stopMemory()
        }
    }

}