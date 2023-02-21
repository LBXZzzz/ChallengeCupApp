package cn.mrra.android.ui.fragment.control

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.FragmentReappearanceBinding
import cn.mrra.android.mrra.MRRA
import cn.mrra.android.storage.room.MemoryRecordDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReappearanceFragment : SimpleFragment<FragmentReappearanceBinding>() {

    override val layoutId: Int = R.layout.fragment_reappearance

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        binding.tvMemoryReappearance.setOnClickListener {
            lifecycleScope.launch {
                val records = withContext(Dispatchers.IO) {
                    MemoryRecordDatabase.selectRecords()
                }
                if (records.isNotEmpty()) {
                    MRRA.INSTANCE.writeReappearance(records)
                } else {
                    toastMsg(getString(R.string.mrra_control_reappearance_empty))
                }
            }
        }
    }

}