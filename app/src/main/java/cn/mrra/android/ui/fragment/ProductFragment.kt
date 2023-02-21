package cn.mrra.android.ui.fragment

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.widget.video.SimpleStatusListener
import cn.mrra.android.common.widget.video.VideoPlayerView
import cn.mrra.android.databinding.FragmentProductBinding
import com.donkingliang.banner.CustomBanner
import com.donkingliang.banner.CustomBanner.ViewCreator
import android.R.array


class ProductFragment : SimpleFragment<FragmentProductBinding>() {

    override val layoutId: Int = R.layout.fragment_product
    private lateinit var mediaController: MediaController

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding.vpvProductVideo) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setAudioFocusRequest(android.media.AudioManager.AUDIOFOCUS_NONE)
            }
            mediaController = MediaController(requireContext())
            mediaController.setAnchorView(this)
            setMediaController(mediaController)
            // setVideoURI(getRawUri(R.raw.video_introduction))
            setOnCompletionListener {
                binding.tvProductTip.visibility = android.view.View.VISIBLE
            }
            setStartListener(object : SimpleStatusListener() {
                override fun onStart(player: VideoPlayerView) {
                    binding.tvProductTip.visibility = android.view.View.GONE
                }
            })
        }

        binding.svProductRoot.setOnScrollChangeListener { _, _, _, _, _ ->
            mediaController.hide()
        }
        binding.tvProductVirtualPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        initBanner()
    }


    override fun onPause() {
        mediaController.hide()
        super.onPause()
    }

    private fun initBanner() {

        val bannerPhotos = ArrayList<Int>()
        bannerPhotos.add(R.drawable.ic_banner_0)
        bannerPhotos.add(R.drawable.ic_banner_1)
        bannerPhotos.add(R.drawable.ic_banner_2)
        bannerPhotos.add(R.drawable.ic_banner_3)
        bannerPhotos.add(R.drawable.ic_banner_4)
        binding.bannerProduct.setPages(object : ViewCreator<Int> {
            override fun createView(context: Context?, position: Int): View {
                //这里返回的是轮播图的项的布局 支持任何的布局
                //position 轮播图的第几个项
                return ImageView(context)
            }

            override fun updateUI(context: Context?, view: View?, position: Int, data: Int) {
                //在这里更新轮播图的UI
                //position 轮播图的第几个项
                //view 轮播图当前项的布局 它是createView方法的返回值
                //data 轮播图当前项对应的数据
                val imView = view as ImageView?
                imView!!.setImageResource(data)
            }
        }, bannerPhotos)
        binding.bannerProduct.startTurning(3000)
    }

}