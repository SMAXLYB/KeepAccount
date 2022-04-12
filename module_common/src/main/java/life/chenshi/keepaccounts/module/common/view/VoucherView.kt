package life.chenshi.keepaccounts.module.common.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import life.chenshi.keepaccounts.module.common.R

/**
 * 票据锯齿效果
 */
class VoucherView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val mPaint = Paint()
    private var mTriangleRadius = 0
    private val mode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private var mBitmap: Bitmap? = null
    private val path = Path()

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.VoucherView)
        mTriangleRadius = typeArray.getDimensionPixelSize(R.styleable.VoucherView_triangleRadius, 5)
        typeArray.recycle()
        initPaint()
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private fun initPaint() {
        mPaint.apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.FILL
            color = 0xffff0000.toInt()
            xfermode = mode
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        initBackground(w, h)
    }

    private fun initBackground(w: Int, h: Int) {
        val bgColor = (background as? ColorDrawable)?.color
        setBackgroundColor(Color.TRANSPARENT)

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mBitmap!!)
        canvas.drawColor(bgColor ?: Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mBitmap!!, 0f, 0f, null)
        // 除去padding
        val num = getTriangleNum(width)
        val remain = width - num * mTriangleRadius * 2
        path.moveTo(remain / 2f, height.toFloat())
        for (i in 1..num * 2) {
            path.lineTo(
                i * mTriangleRadius.toFloat() + remain / 2f,
                height - mTriangleRadius * (i % 2).toFloat()
            )
        }
        path.close()
        canvas.drawPath(path, mPaint)
    }

    private fun getTriangleNum(w: Int): Int =
        w / (mTriangleRadius * 2)
}