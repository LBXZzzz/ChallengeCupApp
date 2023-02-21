package cn.mrra.android.ui.fragment.mrra

import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.widget.video.SimpleStatusListener
import cn.mrra.android.common.widget.video.VideoPlayerView
import cn.mrra.android.databinding.FragmentIntroductionBinding

class IntroductionFragment : SimpleFragment<FragmentIntroductionBinding>() {

    override val layoutId: Int = R.layout.fragment_introduction

    private lateinit var mediaController: MediaController

    override fun onFragmentCreated(savedInstanceState: Bundle?) {
        with(binding.vpvIntroductionVideo) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
            }
            mediaController = MediaController(requireContext())
            mediaController.setAnchorView(this)
            setMediaController(mediaController)
            // setVideoURI(getRawUri(R.raw.video_introduction))
            setOnCompletionListener {
                binding.tvIntroductionTip.visibility = View.VISIBLE
            }
            setStartListener(object : SimpleStatusListener() {
                override fun onStart(player: VideoPlayerView) {
                    binding.tvIntroductionTip.visibility = View.GONE
                }
            })
        }

        binding.svIntroductionRoot.setOnScrollChangeListener { _, _, _, _, _ ->
            mediaController.hide()
        }
    }

    override fun onPause() {
        mediaController.hide()
        super.onPause()
    }

}