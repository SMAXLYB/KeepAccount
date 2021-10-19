package life.chenshi.keepaccounts.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.module.common.utils.dp2px

/**
 * 票券分割线效果
 */
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
    private var dashColor = 0x00000000
    private var bgColor = 0x00000000

    init {
        val typeArray =
            context.obtainStyledAttributes(attrs, R.styleable.TicketDivideLineView)
        dashWidth = typeArray.getDimension(R.styleable.TicketDivideLineView_dashWidth, 5f)
        dashHeight = typeArray.getDimension(R.styleable.TicketDivideLineView_dashHeight, 5f)
        dashInterval = typeArray.getDimension(R.styleable.TicketDivideLineView_dashInterval, 5f)
        dashColor = typeArray.getColor(R.styleable.TicketDivideLineView_dashColor, 0x00000000)
        bgColor = typeArray.getColor(R.styleable.TicketDivideLineView_backgroundColor,0x00000000)
        typeArray.recycle()

        initPaint()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        effect = DashPathEffect(floatArrayOf(dashWidth, dashInterval), 0f)
    }

    private fun initPaint() {
        mPaint.apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.FILL
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
        mPaint.color = bgColor
        canvas.drawRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            mWidth - paddingRight,
            mHeight - paddingBottom,
            mPaint
        )
        // 绘制左半圆
        val r = (mHeight - paddingTop - paddingBottom) / 2
        mPaint.color = dashColor
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
            // style = Paint.Style.STROKE
            pathEffect = effect
            strokeWidth = dashHeight
        }
        canvas.translate(0f,mHeight/2)
        canvas.drawLine(r + paddingLeft+ 2.dp2px(),0f,mWidth - paddingRight - 2.dp2px() -r,0f,mPaint)
    }
}
