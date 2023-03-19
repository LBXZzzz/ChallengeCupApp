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
import androidx.fragment.app.Fragment
import cn.mrra.android.common.base.ACTION_FRAGMENT
import cn.mrra.android.common.base.ACTION_NAVIGATE
import cn.mrra.android.common.startActivity
import cn.mrra.android.ui.activity.MRRAActivity
import cn.mrra.android.ui.fragment.mrra.ControlFragment


class ProductFragment : SimpleFragment<FragmentProductBinding>() {

    override val layoutId: Int = R.layout.fragment_product

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        binding.tvProductPrice2.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        initBanner()
    }


    override fun onPause() {
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