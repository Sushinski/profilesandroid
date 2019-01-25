package ru.profiles.ui.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import ru.profiles.profiles.R
import ru.profiles.utils.Edge
import ru.profiles.utils.PaintUtil

const val DEFAULT_MARGINTOP = 100
const val DEFAULT_MARGINSIDE = 50
const val DEFAULT_BOARDER_COLOR = -0x1
const val DEFAULT_BACKGROUND_COLOR = -0x4fd6cfc1

const val DEFAULT_CROPWIDTH = 600

class CropOverlayView(val mContext: Context, val mAttrs: AttributeSet? = null)  : View(mContext, mAttrs) {

    private var mBorderPaintColor: Int = 0
    private var mBackgroundColor: Int = 0

    private var mMarginTop: Int = 0
    private var mMarginSide: Int = 0

    // The Paint used to darken the surrounding areas outside the crop area.
    private val mBackgroundPaint: Paint = PaintUtil.newBackgroundPaint(mContext)

    // The Paint used to draw the white rectangle around the crop area.
    private var mBorderPaint: Paint = PaintUtil.newBorderPaint(mContext)

    private var cropHeight = DEFAULT_CROPWIDTH
    private var cropWidth = DEFAULT_CROPWIDTH

    private var clipPath: Path = Path()

    private var mBitmapRect: Rect

    private var rectF: RectF

    init {
        val ta = context.obtainStyledAttributes(mAttrs, R.styleable.CropOverlayView, 0, 0)
        try {
            mMarginTop = ta.getDimensionPixelSize(R.styleable.CropOverlayView_marginTop, DEFAULT_MARGINTOP)
            mMarginSide = ta.getDimensionPixelSize(R.styleable.CropOverlayView_marginSide, DEFAULT_MARGINSIDE)
            mBorderPaintColor = ta.getColor(R.styleable.CropOverlayView_borderColor, DEFAULT_BOARDER_COLOR)
            mBackgroundColor = ta.getColor(R.styleable.CropOverlayView_overlayColor, DEFAULT_BACKGROUND_COLOR)
        } finally {
            ta.recycle()
        }

        val w = context.resources.displayMetrics.widthPixels
        cropWidth = w - 2 * mMarginSide
        cropHeight = cropWidth
        val edgeT = mMarginTop
        val edgeB = mMarginTop + cropHeight
        val edgeL = mMarginSide
        val edgeR = mMarginSide + cropWidth
        mBackgroundPaint.color = mBackgroundColor
        mBorderPaint.color = mBorderPaintColor
        Edge.TOP.coordinate = edgeT.toFloat()
        Edge.BOTTOM.coordinate = edgeB.toFloat()
        Edge.LEFT.coordinate = edgeL.toFloat()
        Edge.RIGHT.coordinate = edgeR.toFloat()
        Rect(edgeL, edgeT, edgeR, edgeB)
        mBitmapRect = Rect(0, 0, w, w)
        rectF = RectF(
            Edge.LEFT.coordinate,
            Edge.TOP.coordinate,
            Edge.RIGHT.coordinate,
            Edge.BOTTOM.coordinate
        )

    }

    fun getImageBounds(): Rect {
        return Rect(
            Edge.LEFT.coordinate.toInt(),
            Edge.TOP.coordinate.toInt(),
            Edge.RIGHT.coordinate.toInt(),
            Edge.BOTTOM.coordinate.toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
       val cx = (Edge.LEFT.coordinate + Edge.RIGHT.coordinate) / 2
        val cy = (Edge.TOP.coordinate + Edge.BOTTOM.coordinate) / 2
        val radius2 = (Edge.RIGHT.coordinate - Edge.LEFT.coordinate) / 2
        clipPath.addCircle(cx, cy, radius2, Path.Direction.CW)
        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)
        canvas.drawColor(mBackgroundColor)
        if (Build.VERSION.SDK_INT < 23) canvas.restore()
        canvas.drawCircle(cx, cy, radius2, mBorderPaint)
    }




}