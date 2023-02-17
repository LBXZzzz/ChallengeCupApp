package cn.mrra.android.common.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import cn.mrra.android.common.R
import kotlin.math.max
import kotlin.math.min

class CircleView : View {

    companion object {
        private const val CIRCLE_RADIUS_REDUCE = 4F
    }

    private val mCirclePaint: Paint
    private val mTextPaint: TextPaint
    private var mText: String
    private var mTextSize: Int
    private var mTextColor: Int
    private var mIsBold: Boolean

    internal constructor (context: Context)
            : this(context, null)

    internal constructor (context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    internal constructor (context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.CircleView
        ).run {
            mText = getString(R.styleable.CircleView_text) ?: "MRRA"
            mTextSize = getDimensionPixelSize(R.styleable.CircleView_textSize, 108)
            mTextColor = getColor(R.styleable.CircleView_textColor, Color.BLACK)
            mIsBold = getBoolean(R.styleable.CircleView_isBold, true)
            recycle()
        }

        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = CIRCLE_RADIUS_REDUCE
        }

        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mTextColor
            textSize = mTextSize.toFloat()
            isFakeBoldText = mIsBold
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mCirclePaint.shader = LinearGradient(
            0F, 0F, width.toFloat(), height.toFloat(),
            Array(7) {
                when (it) {
                    0 -> Color.rgb(0XFF, 0X00, 0X00)
                    1 -> Color.rgb(0xFF, 0x7F, 0x00)
                    2 -> Color.rgb(0XFF, 0XFF, 0X00)
                    3 -> Color.rgb(0X00, 0XFF, 0X00)
                    4 -> Color.rgb(0X00, 0XFF, 0XFF)
                    5 -> Color.rgb(0X00, 0X00, 0XFF)
                    6 -> Color.rgb(0X8B, 0X00, 0XFF)
                    else -> Color.TRANSPARENT
                }
            }.toIntArray(),
            null, Shader.TileMode.CLAMP
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        return setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    private val mRect = Rect()

    private var mRadiusPercent: Float = 1F

    override fun onDraw(canvas: Canvas) {
        var radius = mRadiusPercent * (min(width, height) / 2F - 5F)
        mTextPaint.textSize = mRadiusPercent * mTextSize
        for (i in 40 downTo 15) {
            mCirclePaint.alpha = 100 * (i - 15) / 25
            canvas.drawCircle(width / 2F, height / 2F, radius, mCirclePaint)
            radius -= CIRCLE_RADIUS_REDUCE
            if (radius < 20F) break
        }
        mTextPaint.getTextBounds(mText, 0, mText.length, mRect)
        canvas.drawText(
            mText,
            (width - mRect.width()) / 2F,
            (height + mRect.height()) / 2F,
            mTextPaint
        )
    }

    @Suppress("UNUSED")
    private fun setCircleRadius(percent: Int) {
        if (percent < 90 || percent > 100) return
        mRadiusPercent = percent / 100F
        invalidate()
    }

    /**
     * 恢复初始大小
     * */
    fun scaleUp() {
        mOnClickReductionAnimator?.cancel()
        mOnClickAmplificationAnimator?.cancel()
        if (getCircleRadius() != 100) {
            mOnClickAmplificationAnimator = ObjectAnimator.ofInt(
                this,
                "CircleRadius",
                getCircleRadius(),
                100
            ).apply {
                duration = 200L
                start()
            }
        }
    }

    /**
     * 缩小
     * */
    fun scaleDown() {
        mOnClickReductionAnimator?.cancel()
        mOnClickAmplificationAnimator?.cancel()
        mDownTime = SystemClock.uptimeMillis()
        mOnClickReductionAnimator = ObjectAnimator.ofInt(
            this,
            "CircleRadius",
            getCircleRadius(),
            94
        ).apply {
            duration = 200L
            start()
        }
    }

    var text: String
        get() = mText
        set(value) {
            mText = value
            invalidate()
        }

    private fun getCircleRadius(): Int = (100 * mRadiusPercent).toInt()

    private var mOnClickListener: ((CircleView) -> Boolean)? = null

    fun setOnClickListener(listener: (CircleView) -> Boolean) {
        mOnClickListener = listener
    }

    private var mOnClickReductionAnimator: ObjectAnimator? = null
    private var mOnClickAmplificationAnimator: ObjectAnimator? = null

    private var mDownTime = 0L

    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (isEnabled) scaleDown()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isEnabled)
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (mOnClickListener?.invoke(this) != false) {
                            mOnClickAmplificationAnimator = ObjectAnimator.ofInt(
                                this,
                                "CircleRadius",
                                94,
                                100
                            ).apply {
                                duration = 200L
                                start()
                            }
                        }
                    }, max(0, 200L - (SystemClock.uptimeMillis() - mDownTime)))
            }
        }
        return true
    }

}