package cn.mrra.android.ui.fragment

import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.widget.video.SimpleStatusListener
import cn.mrra.android.common.widget.video.VideoPlayerView
import cn.mrra.android.databinding.FragmentProductBinding

class ProductFragment : SimpleFragment<FragmentProductBinding>() {

    override val layoutId: Int = R.layout.fragment_product

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
    }

}