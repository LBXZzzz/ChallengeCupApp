package cn.mrra.android.common.widget.video

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class VideoPlayerView : VideoView {

    internal constructor (context: Context)
            : this(context, null)

    internal constructor (context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    internal constructor (context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private var startListener: IStatusListener? = null

    fun setStartListener(listener: IStatusListener?) {
        startListener = listener
    }

    override fun start() {
        super.start()
        startListener?.onStart(this)
        startListener?.onStatusChanged(PlayerStatus.Start, this)
    }

    override fun pause() {
        super.pause()
        startListener?.onPause(this)
        startListener?.onStatusChanged(PlayerStatus.Pause, this)
    }

    override fun resume() {
        super.resume()
        startListener?.onResume(this)
        startListener?.onStatusChanged(PlayerStatus.Resume, this)
    }

    override fun suspend() {
        super.suspend()
        startListener?.onSuspend(this)
        startListener?.onStatusChanged(PlayerStatus.Suspend, this)
    }

    enum class PlayerStatus {
        Start,
        Pause,
        Resume,
        Suspend
    }

}