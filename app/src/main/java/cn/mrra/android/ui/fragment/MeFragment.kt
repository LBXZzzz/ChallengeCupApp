package cn.mrra.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.mrra.android.common.base.BaseFragment
import cn.mrra.android.databinding.FragmentMeBinding

class MeFragment : BaseFragment<FragmentMeBinding>() {

    override var _binding: FragmentMeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeBinding.inflate(inflater, container, false)
        return binding.root
    }

}