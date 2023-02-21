package cn.mrra.android.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet

class TextViewMarqueeForever : androidx.appcompat.widget.AppCompatTextView {

    private var mFocuses = true

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        isSingleLine = true
        marqueeRepeatLimit = -1
    }

    override fun isFocused() = mFocuses

    fun setFocused(isFocused: Boolean) {
        mFocuses = isFocused
    }
}