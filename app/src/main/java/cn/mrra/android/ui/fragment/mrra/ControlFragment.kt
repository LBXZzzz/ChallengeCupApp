package cn.mrra.android.ui.fragment.mrra

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.databinding.FragmentControlBinding
import cn.mrra.android.entity.ControlMode
import cn.mrra.android.ui.fragment.control.InitiativeFragment
import cn.mrra.android.ui.fragment.control.MemoryFragment
import cn.mrra.android.ui.fragment.control.PassiveFragment
import cn.mrra.android.ui.fragment.control.StopFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ControlFragment : SimpleFragment<FragmentControlBinding>() {

    override val layoutId: Int = R.layout.fragment_control

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding) {
            vpControlViewPager.adapter = ControlModeViewPagerAdapter(this@ControlFragment)
            vpControlViewPager.offscreenPageLimit = ControlModeViewPagerAdapter.titles.size
            vpControlViewPager.currentItem = 0

            tabControlModeSelect.tabMode = TabLayout.MODE_AUTO
            TabLayoutMediator(tabControlModeSelect, vpControlViewPager, true) { tab, position ->
                tab.setText(ControlModeViewPagerAdapter.titles[position])
            }.attach()
        }
    }

    class ControlModeViewPagerAdapter(
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        companion object {
            internal val titles = arrayOf(
                ControlMode.STOP.label,
                ControlMode.PASSIVE.label,
                ControlMode.INITIATIVE.label,
                ControlMode.MEMORY.label
            )
        }

        override fun getItemCount() = titles.size

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> StopFragment()
                1 -> PassiveFragment()
                2 -> InitiativeFragment()
                else -> MemoryFragment()
            }
        }
    }

}