package cn.mrra.android.common.widget.video

interface IStatusListener {
    fun onStart(player: VideoPlayerView)

    fun onPause(player: VideoPlayerView)

    fun onResume(player: VideoPlayerView)

    fun onSuspend(player: VideoPlayerView)

    fun onStatusChanged(status: VideoPlayerView.PlayerStatus, player: VideoPlayerView)
}