package cn.mrra.android.ui.fragment

import android.os.Bundle
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.databinding.FragmentProductBinding

class ProductFragment : SimpleFragment<FragmentProductBinding>() {

    override val layoutId: Int = R.layout.fragment_product

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
    }

}