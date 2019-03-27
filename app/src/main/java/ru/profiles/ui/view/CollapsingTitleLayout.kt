package ru.profiles.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import ru.profiles.profiles.R


class CollapsingTitleLayout: FrameLayout {

    private var mToolbar: Toolbar? = null
    private var mDummyView: View? = null

    private var mScrollOffset: Float = 0.toFloat()

    private var mToolbarContentBounds: Rect = Rect()

    private var mExpandedMarginLeft: Float = 0.toFloat()
    private var mExpandedMarginRight: Float = 0.toFloat()
    private var mExpandedMarginBottom: Float = 0.toFloat()

    private var mRequestedExpandedTitleTextSize: Int = 0
    private var mExpandedTitleTextSize: Int = 0
    private var mCollapsedTitleTextSize: Int = 0

    private var mExpandedTop: Float = 0.toFloat()
    private var mCollapsedTop: Float = 0.toFloat()

    private var mTitle: String = String()
    private var mTitleToDraw: String = String()
    private var mUseTexture: Boolean = false
    private var mExpandedTitleTexture: Bitmap? = null

    private var mTextLeft: Float = 0.toFloat()
    private var mTextRight: Float = 0.toFloat()
    private var mTextTop: Float = 0.toFloat()

    private var mScale: Float = 0.toFloat()

    private var mTextPaint: TextPaint = TextPaint().also { it.isAntiAlias = true }
    private var mTexturePaint: Paint? = null

    private var mTextSizeInterpolator: Interpolator

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.CollapsingTitleLayout)
        mExpandedMarginBottom = a.getDimensionPixelSize(R.styleable.CollapsingTitleLayout_expandedMargin, 0).toFloat()
        mExpandedMarginRight = mExpandedMarginBottom
        mExpandedMarginLeft = mExpandedMarginRight

        val isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
        if (a.hasValue(R.styleable.CollapsingTitleLayout_expandedMarginStart)) {
            val marginStart = a.getDimensionPixelSize(
                R.styleable.CollapsingTitleLayout_expandedMarginStart, 0
            )
            if (isRtl) {
                mExpandedMarginRight = marginStart.toFloat()
            } else {
                mExpandedMarginLeft = marginStart.toFloat()
            }
        }
        if (a.hasValue(R.styleable.CollapsingTitleLayout_expandedMarginEnd)) {
            val marginEnd = a.getDimensionPixelSize(
                R.styleable.CollapsingTitleLayout_expandedMarginEnd, 0
            )
            if (isRtl) {
                mExpandedMarginLeft = marginEnd.toFloat()
            } else {
                mExpandedMarginRight = marginEnd.toFloat()
            }
        }
        if (a.hasValue(R.styleable.CollapsingTitleLayout_expandedMarginBottom)) {
            mExpandedMarginBottom = a.getDimensionPixelSize(
                R.styleable.CollapsingTitleLayout_expandedMarginBottom, 0
            ).toFloat()
        }

        val tp = a.getResourceId(
            R.styleable.CollapsingTitleLayout_android_textAppearance,
            android.R.style.TextAppearance
        )
        setTextAppearance(tp)

        if (a.hasValue(R.styleable.CollapsingTitleLayout_collapsedTextSize)) {
            mCollapsedTitleTextSize = a.getDimensionPixelSize(
                R.styleable.CollapsingTitleLayout_collapsedTextSize, 0
            )
        }

        mRequestedExpandedTitleTextSize = a.getDimensionPixelSize(
            R.styleable.CollapsingTitleLayout_expandedTextSize, mCollapsedTitleTextSize
        )

        val interpolatorId = a
            .getResourceId(
                R.styleable.CollapsingTitleLayout_textSizeInterpolator,
                android.R.anim.accelerate_interpolator
            )
        mTextSizeInterpolator = AnimationUtils.loadInterpolator(context, interpolatorId)

        a.recycle()

        setWillNotDraw(false)
    }

    fun setTextAppearance(resId: Int) {
        val atp = context.obtainStyledAttributes(
            resId,
            R.styleable.CollapsingTextAppearance
        )
        mTextPaint.color = atp.getColor(
            R.styleable.CollapsingTextAppearance_android_textColor, Color.WHITE
        )
        mCollapsedTitleTextSize = atp.getDimensionPixelSize(
            R.styleable.CollapsingTextAppearance_android_textSize, 0
        )
        atp.recycle()

        recalculate()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)

        if (child is Toolbar) {
            mToolbar = child
            mDummyView = View(context)
            mToolbar?.addView(mDummyView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        }
    }

    /**
     * Set the value indicating the current scroll value. This decides how much of the
     * background will be displayed, as well as the title metrics/positioning.
     *
     * A value of `0.0` indicates that the layout is fully expanded.
     * A value of `1.0` indicates that the layout is fully collapsed.
     */
    fun setScrollOffset(offset: Float) {
        if (offset != mScrollOffset) {
            mScrollOffset = offset
            calculateOffsets()
        }
    }

    private fun calculateOffsets() {
        val offset = mScrollOffset
        val textSizeOffset = mTextSizeInterpolator.getInterpolation(mScrollOffset)

        mTextLeft = interpolate(mExpandedMarginLeft, mToolbarContentBounds.left.toFloat(), offset)
        mTextTop = interpolate(mExpandedTop, mCollapsedTop, offset)
        mTextRight = interpolate(width - mExpandedMarginRight, mToolbarContentBounds.right.toFloat(), offset)

        setInterpolatedTextSize(
            interpolate(mExpandedTitleTextSize.toFloat(),
                mCollapsedTitleTextSize.toFloat(),
                textSizeOffset)
        )
        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun calculateTextBounds() {
        val metrics = resources.displayMetrics

        // We then calculate the collapsed text size, using the same logic
        mTextPaint.setTextSize(mCollapsedTitleTextSize.toFloat())
        val textHeight = mTextPaint.descent() - mTextPaint.ascent()
        val textOffset = textHeight / 2 - mTextPaint.descent()
        mCollapsedTop = mToolbarContentBounds.centerY() + textOffset

        // First, let's calculate the expanded text size so that it fit within the bounds
        // We make sure this value is at least our minimum text size
        mExpandedTitleTextSize = Math.max(
            mCollapsedTitleTextSize,
            getSingleLineTextSize(
                mTitle, mTextPaint,
                width - mExpandedMarginLeft - mExpandedMarginRight, 0f,
                mRequestedExpandedTitleTextSize.toFloat(), 0.5f, metrics
            ).toInt()
        )
        mExpandedTop = height - mExpandedMarginBottom

        // The bounds have changed so we need to clear the texture
        clearTexture()
    }

    override fun draw(canvas: Canvas) {
        val saveCount = canvas.save()

        val toolbarHeight = mToolbar?.height
        canvas.clipRect(
            0f, 0f, canvas.width.toFloat(),
            interpolate(canvas.height.toFloat(), toolbarHeight?.toFloat() ?: 0f, mScrollOffset)
        )

        // Now call super and let it draw the background, etc
        super.draw(canvas)

        if (mTitleToDraw != null) {
            val x = mTextLeft
            var y = mTextTop

            val ascent = mTextPaint?.ascent()?.times(mScale)
            val descent = mTextPaint?.descent()?.times(mScale)
            val h = ascent?.let{descent?.minus(it)}

           /* if (DEBUG_DRAW) {
                // Just a debug tool, which drawn a Magneta rect in the text bounds
                canvas.drawRect(
                    mTextLeft,
                    y - h + descent,
                    mTextRight,
                    y + descent,
                    DEBUG_DRAW_PAINT
                )
            }*/

            if (mUseTexture) {
                y = y.minus(h ?: 0.0f).plus(descent ?: 0.0f)
            }

            if (!mScale.equals(1.0f)) {
                canvas.scale(mScale, mScale, x, y)
            }

            if (mUseTexture && mExpandedTitleTexture != null) {
                // If we should use a texture, draw it instead of text
                canvas.drawBitmap(mExpandedTitleTexture, x, y, mTexturePaint)
            } else {
                canvas.drawText(mTitleToDraw, x, y, mTextPaint)
            }
        }

        canvas.restoreToCount(saveCount)
    }

    private fun setInterpolatedTextSize(textSize: Float) {
        if (mTitle.isEmpty()) return

        if (isClose(textSize, mCollapsedTitleTextSize.toFloat())
            || isClose(textSize, mExpandedTitleTextSize.toFloat())
        ) {
            // If the text size is 'close' to being a decimal, then we use this as a sync-point.
            // We disable our manual scaling and set the paint's text size.
            mTextPaint.setTextSize(textSize)
            mScale = 1f

            // We also use this as an opportunity to ellipsize the string
            val title = TextUtils.ellipsize(
                mTitle, mTextPaint,
                mTextRight - mTextLeft,
                TextUtils.TruncateAt.END
            )
            if (title !== mTitleToDraw) {
                // If the title has changed, turn it into a string
                mTitleToDraw = title.toString()
            }

            mUseTexture = false
        } else {
            // We're not close to a decimal so use our canvas scaling method
            if (mExpandedTitleTexture != null) {
                mScale = textSize / mExpandedTitleTextSize
            } else {
                mScale = textSize / mTextPaint.getTextSize()
            }

            mUseTexture = false
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }


    private fun ensureExpandedTexture() {
        if (mExpandedTitleTexture != null) return
        val w = (width - mExpandedMarginLeft - mExpandedMarginRight).toInt()
        val h = (mTextPaint.descent().minus(mTextPaint.ascent())).toInt()
        mExpandedTitleTexture = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        mExpandedTitleTexture?.let{
            b->Canvas(b)
        }?.also{
            c->c.drawText(mTitleToDraw, 0f, mTextPaint.descent().let{h - it}, mTextPaint)
        }

        if (mTexturePaint == null) {
            // Make sure we have a paint
            mTexturePaint = Paint()
            mTexturePaint?.isAntiAlias = true
            mTexturePaint?.isFilterBitmap = true
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mToolbarContentBounds.left = mDummyView?.left ?: 0
        mToolbarContentBounds.top = mDummyView?.top ?: 0
        mToolbarContentBounds.right = mDummyView?.right ?: 0
        mToolbarContentBounds.bottom = mDummyView?.bottom ?: 0

        if (changed) {
            // If we've changed and we have a title, re-calculate everything!
            recalculate()
        }
    }

    private fun recalculate() {
        if (height > 0) {
            calculateTextBounds()
            calculateOffsets()
        }
    }

    /**
     * Set the title to display
     *
     * @param title
     */
    fun setTitle(title: String?) {
        if (title == null || title != mTitle) {
            mTitle = title ?: String()

            clearTexture()

            if (height > 0) {
                // If we've already been laid out, calculate everything now otherwise we'll wait
                // until a layout
                recalculate()
            }
        }
    }

    private fun clearTexture() {
        if (mExpandedTitleTexture != null) {
            mExpandedTitleTexture?.recycle()
            mExpandedTitleTexture = null
        }
    }

    /**
     * Recursive binary search to find the best size for the text
     *
     * Adapted from https://github.com/grantland/android-autofittextview
     */
    private fun getSingleLineTextSize(
        text: String, paint: TextPaint, targetWidth: Float,
        low: Float, high: Float, precision: Float, metrics: DisplayMetrics
    ): Float {
        val mid = (low + high) / 2.0f

        paint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mid, metrics)
        val maxLineWidth = paint.measureText(text)

        return when {
            high - low < precision -> low
            maxLineWidth > targetWidth -> getSingleLineTextSize(text, paint, targetWidth, low, mid, precision, metrics)
            maxLineWidth < targetWidth -> getSingleLineTextSize(text, paint, targetWidth, mid, high, precision, metrics)
            else -> mid
        }
    }

    /**
     * Returns true if `value` is 'close' to it's closest decimal value. Close is currently
     * defined as it's difference being < 0.01.
     */
    private fun isClose(value: Float, targetValue: Float): Boolean {
        return Math.abs(value - targetValue) < 0.01f
    }

    /**
     * Interpolate between `startValue` and `endValue`, using `progress`.
     */
    private fun interpolate(startValue: Float, endValue: Float, progress: Float): Float {
        return startValue + (endValue - startValue) * progress
    }


}