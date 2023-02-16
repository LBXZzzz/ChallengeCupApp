package cn.mrra.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.mrra.android.common.base.BaseFragment
import cn.mrra.android.databinding.FragmentMrraBinding

class MRRAFragment : BaseFragment<FragmentMrraBinding>() {

    override var _binding: FragmentMrraBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMrraBinding.inflate(inflater, container, false)
        return binding.root
    }

}