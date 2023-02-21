package cn.mrra.android.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.databinding.FragmentMrraBinding
import cn.mrra.android.ui.fragment.mrra.ContactFragment
import cn.mrra.android.ui.fragment.mrra.ControlFragment
import cn.mrra.android.ui.fragment.mrra.IntroductionFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MRRAFragment : SimpleFragment<FragmentMrraBinding>() {

    override val layoutId: Int = R.layout.fragment_mrra

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding) {
            vpMrraViewPager.adapter = MRRAFragmentViewPagerAdapter(
                this@MRRAFragment
            )
            vpMrraViewPager.offscreenPageLimit = MRRAFragmentViewPagerAdapter.titles.size
            vpMrraViewPager.currentItem = 0

            tbMrraTab.tabMode = TabLayout.MODE_FIXED
            TabLayoutMediator(tbMrraTab, vpMrraViewPager, true) { tab, position ->
                tab.setText(MRRAFragmentViewPagerAdapter.titles[position])
            }.attach()
        }
    }

    class MRRAFragmentViewPagerAdapter(
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        companion object {
            internal val titles = arrayOf(
                R.string.mrra_control,
                R.string.mrra_introduction,
                R.string.mrra_contact_us
            )
        }

        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ControlFragment()
                1 -> IntroductionFragment()
                else -> ContactFragment()
            }
        }
    }

}