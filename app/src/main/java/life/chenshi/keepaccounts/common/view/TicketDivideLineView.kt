package life.chenshi.keepaccounts.common.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import life.chenshi.keepaccounts.R

class TicketDivideLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private val DEFAULT_HEIGHT = 20.dp2px()
    }

    private val mPaint = Paint()
    private var mWidth = 0f
    private var mHeight = 0f

    private var dashWidth: Float = 0f
    private var dashHeight: Float = 0f
    private var dashInterval: Float = 0f

    private var effect: DashPathEffect
    private val mode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private val path = Path()

    init {
        val typeArray =
            context.obtainStyledAttributes(attrs, R.styleable.TicketDivideLineView)
        dashWidth = typeArray.getDimension(R.styleable.TicketDivideLineView_dashWidth, 5f)
        dashHeight = typeArray.getDimension(R.styleable.TicketDivideLineView_dashHeight, 5f)
        dashInterval = typeArray.getDimension(R.styleable.TicketDivideLineView_dashInterval, 5f)
        typeArray.recycle()

        initPaint()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        effect = DashPathEffect(floatArrayOf(dashWidth, dashInterval), 0f)
    }

    private fun initPaint() {
        mPaint.apply {
            isAntiAlias = true
            isDither = true
            color = if (background is ColorDrawable) {
                (background as ColorDrawable).color
            } else {
                0xfffffff
            }
            style = Paint.Style.FILL
            background = null
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = resolveSize(DEFAULT_HEIGHT, heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {

        // 新建图层
        val savedLayer = canvas.saveLayer(0f, 0f, mWidth, mHeight, mPaint)

        // 绘制背景
        canvas.drawRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            mWidth - paddingRight,
            mHeight - paddingBottom,
            mPaint
        )

        // 设置混合模式
        mPaint.xfermode = mode

        // 绘制左半圆
        val r = (mHeight - paddingTop - paddingBottom) / 2
        mPaint.color = 0xffff0000.toInt()
        canvas.drawArc(
            -r + paddingLeft,
            paddingTop.toFloat(),
            r + paddingLeft,
            mHeight - paddingBottom,
            270f,
            180f,
            true,
            mPaint
        )

        // 绘制右半圆
        canvas.drawArc(
            mWidth - paddingRight - r,
            paddingTop.toFloat(),
            r + mWidth - paddingRight,
            mHeight - paddingBottom,
            90f,
            180f,
            true,
            mPaint
        )

        // 绘制虚线
        mPaint.apply {
            style = Paint.Style.STROKE
            pathEffect = effect
            strokeWidth = dashHeight
        }
        path.moveTo(r + paddingLeft.toFloat() + 2.dp2px(), mHeight / 2)
        path.lineTo(mWidth - paddingRight - 2.dp2px(), mHeight / 2)
        canvas.drawPath(path, mPaint)

        canvas.restoreToCount(savedLayer)
    }
}
