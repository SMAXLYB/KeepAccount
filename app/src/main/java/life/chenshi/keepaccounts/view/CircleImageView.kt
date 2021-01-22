package life.chenshi.keepaccounts.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import life.chenshi.keepaccounts.R


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    // 可配置属性
    private var mBorderWidth = 0
    private var mBorderColor = Color.WHITE
    private var mShowShadow = false
    private var mShadowColor = Color.BLACK
    private var mShadowRadius = 4.0f

    private var bitmap: Bitmap? = null

    // 图片直径
    private var mBitmapDiameter = 120f

    // 图片画笔
    private lateinit var mBitmapPaint: Paint

    // 边框画笔
    private var mBorderPaint: Paint? = null

    // 渲染器
    private val mBitmapShader: BitmapShader? = null

    // 控件宽高
    private var mWidthOrHeight = 0.0f

    // 父view给的测量宽高
    private var mWidthSpecSize: Float = 0.0f
    private var mHeightSpecSize: Float = 0.0f

    init {
        initData(context, attrs)
    }

    /**
     * 初始化配置属性
     */
    private fun initData(context: Context, attrs: AttributeSet?) {
        // 如果用户有自定义参数
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            mBorderWidth = typedArray.getInt(R.styleable.CircleImageView_borderWidth, mBorderWidth)
            mBorderColor =
                typedArray.getColor(R.styleable.CircleImageView_borderColor, mBorderColor)
            mShowShadow = typedArray.getBoolean(R.styleable.CircleImageView_showShadow, mShowShadow)
            mShadowColor =
                typedArray.getColor(R.styleable.CircleImageView_shadowColor, mShadowColor)
            mShadowRadius =
                typedArray.getFloat(R.styleable.CircleImageView_shadowRadius, mShadowRadius)
            typedArray.recycle()
        }

        // 初始化图片画笔
        mBitmapPaint = Paint().apply {
            isAntiAlias = true
        }

        // 初始化边框画笔
        mBorderPaint = Paint().apply {
            isAntiAlias = true
            color = mBorderColor
            if (mShowShadow) {
                setShadowLayer(mShadowRadius, 0f, 0f, mShadowColor)
            }
        }

        // 开启硬件加速 todo 什么作用
        this.setLayerType(LAYER_TYPE_HARDWARE, mBorderPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 获取父view给的测量要求和大小
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        mWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        mHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        // 宽高等于默认直径加边框宽度
        mWidthOrHeight = mBitmapDiameter + (mBorderWidth * 2)

        // 根据父view给的要求做测量
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {

        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}