package cn.mrra.android.ui.fragment

import android.os.Bundle
import android.widget.MediaController
import cn.mrra.android.R
import cn.mrra.android.common.base.SimpleFragment
import cn.mrra.android.common.widget.video.SimpleStatusListener
import cn.mrra.android.common.widget.video.VideoPlayerView
import cn.mrra.android.databinding.FragmentMrraBinding

class MRRAFragment : SimpleFragment<FragmentMrraBinding>() {

    override val layoutId: Int = R.layout.fragment_mrra

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
    }

    override fun onPause() {
        mediaController.hide()
        super.onPause()
    }

}