package cn.mrra.android.ui.fragment.mrra

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.toastMsg
import cn.mrra.android.databinding.FragmentControlBinding
import cn.mrra.android.entity.ConnectStatus
import cn.mrra.android.entity.ControlMode
import cn.mrra.android.mrra.MRRA
import cn.mrra.android.ui.fragment.control.*
import cn.mrra.android.ui.fragment.control.DataFrame.Companion.formatP0ToAngle0
import cn.mrra.android.ui.fragment.control.DataFrame.Companion.formatP1ToAngle1
import cn.mrra.android.ui.fragment.control.DataFrame.Companion.formatP2ToAngle2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ControlFragment : SimpleFragment<FragmentControlBinding>() {

    override val layoutId: Int = R.layout.fragment_control

    @Suppress("MissingPermission", "SetTextI18n")
    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding) {
            vpControlViewPager.adapter = ControlModeViewPagerAdapter(this@ControlFragment)
            vpControlViewPager.offscreenPageLimit = ControlModeViewPagerAdapter.titles.size
            vpControlViewPager.currentItem = 0

            tabControlModeSelect.tabMode = TabLayout.MODE_AUTO
            TabLayoutMediator(tabControlModeSelect, vpControlViewPager, true) { tab, position ->
                tab.setText(ControlModeViewPagerAdapter.titles[position])
            }.attach()

            lifecycleScope.launch {
                MRRA.INSTANCE.controller.leDevice.collect {
                    tvControlDeviceName.text = if (it != null) {
                        it.name ?: it.address
                    } else {
                        getString(R.string.mrra_control_not_connected)
                    }
                }
            }

            lifecycleScope.launch {
                MRRA.INSTANCE.controller.leDevice.collect {
                    tvControlMacAddress.text = if (it != null) {
                        it.address
                    } else {
                        getString(R.string.mrra_control_not_connected)
                    }
                }
            }

            lifecycleScope.launch {
                MRRA.INSTANCE.status.collect {
                    tvControlConnectStatus.text = getString(it.label)
                }
            }

            lifecycleScope.launch {
                MRRA.INSTANCE.mode.collect {
                    tvControlMode.text = getString(it.label)
                }
            }

            lifecycleScope.launch {
                MRRA.INSTANCE.dataFrame.collect {
                    if (MRRA.INSTANCE.status.value == ConnectStatus.SUCCESS) {
                        tvControlAngle0.text = "${it.p0.formatP0ToAngle0()}°"
                        tvControlAngle1.text = "${it.p1.formatP1ToAngle1()}°"
                        tvControlAngle2.text = "${it.p2.formatP2ToAngle2()}°"
                    } else {
                        tvControlAngle0.text = "-°"
                        tvControlAngle1.text = "-°"
                        tvControlAngle2.text = "-°"
                    }
                }
            }
        }
    }

    class ControlModeViewPagerAdapter(
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        companion object {
            internal val titles = arrayOf(
                ControlMode.INITIATIVE.label,
                ControlMode.PASSIVE.label,
                ControlMode.MEMORY.label,
                ControlMode.REAPPEARANCE.label,
                ControlMode.STOP.label
            )
        }

        override fun getItemCount() = titles.size

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> InitiativeFragment()
                1 -> PassiveFragment()
                2 -> MemoryFragment()
                3 -> ReappearanceFragment()
                else -> StopFragment()
            }
        }
    }

}