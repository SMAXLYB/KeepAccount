package life.chenshi.keepaccounts.module.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import life.chenshi.keepaccounts.module.common.R
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min


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

    private var mBitmap: Bitmap? = null

    // 图片直径
    private var mBitmapDiameter = 120f

    // 图片画笔
    private lateinit var mBitmapPaint: Paint

    // 边框画笔
    private var mBorderPaint: Paint? = null

    // 渲染器
    private var mBitmapShader: BitmapShader? = null

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
            mBorderWidth = typedArray.getInt(R.styleable.CircleImageView_borderWidths, mBorderWidth)
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
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取父view给的测量要求和大小
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        mWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        mHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        // 宽高等于默认直径加边框宽度
        mWidthOrHeight = mBitmapDiameter + (mBorderWidth * 2)

        // 如果给定了宽高
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            // 如果宽度大于高度, 以高度为准
            if (mWidthSpecSize > mHeightSpecSize) {
                mBitmapDiameter = mHeightSpecSize - (mBorderWidth * 2)
                mWidthOrHeight = mHeightSpecSize
            } else {
                mBitmapDiameter = mWidthSpecSize - (mBorderWidth * 2)
                mWidthOrHeight = mWidthSpecSize
            }
        } else if (widthMode == MeasureSpec.EXACTLY) {
            mBitmapDiameter = mWidthSpecSize - (mBorderWidth * 2)
            mWidthOrHeight = mWidthSpecSize
        } else if (heightMode == MeasureSpec.EXACTLY) {
            mBitmapDiameter = mHeightSpecSize - (mBorderWidth * 2)
            mWidthOrHeight = mHeightSpecSize
        }

        setMeasuredDimension(
            ceil((mWidthOrHeight + 2).toDouble()).toInt(),
            ceil((mWidthOrHeight + 2).toDouble()).toInt()
        )
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        loadBitmap()
        mBitmap = centerSquareScaleBitmap(mBitmap!!, ceil(mBitmapDiameter.toDouble() + 1).toInt())
        mBitmap?.let {
            mBitmapShader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            mBitmapPaint.shader = mBitmapShader
            if (mShowShadow) {
                mBorderPaint?.setShadowLayer(mShadowRadius, 0f, 0f, mShadowColor)
                // 先画边框
                canvas.drawCircle(
                    mWidthOrHeight / 2,
                    mWidthOrHeight / 2,
                    mBitmapDiameter / 2 + mBorderWidth - mShadowRadius,
                    mBorderPaint!!
                )
                // 后画图片
                canvas.drawCircle(
                    mWidthOrHeight / 2,
                    mWidthOrHeight / 2,
                    mBitmapDiameter / 2 - mShadowRadius,
                    mBitmapPaint
                )
            } else {
                mBorderPaint?.setShadowLayer(0f, 0f, 0f, mShadowColor)
                canvas.drawCircle(
                    mWidthOrHeight / 2,
                    mWidthOrHeight / 2,
                    mBitmapDiameter / 2 + mBorderWidth,
                    mBorderPaint!!
                )
                canvas.drawCircle(
                    mWidthOrHeight / 2,
                    mWidthOrHeight / 2,
                    mBitmapDiameter / 2,
                    mBitmapPaint
                )
            }
        }
    }

    private fun loadBitmap() {
        val bitmapDrawable = drawable
        mBitmap = bitmapDrawable?.toBitmap()
    }

    /**
     * 裁切图片,获得中间正方形的图片
     * @param edgeLength 边长
     */
    private fun centerSquareScaleBitmap(bitmap: Bitmap, edgeLength: Int): Bitmap {
        if (edgeLength <= 0) {
            return bitmap
        }

        var result = bitmap

        val widthOrg = bitmap.width
        val heightOrg = bitmap.height

        // 先缩放,后裁切
        if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
            // 计算最长边按边长比例缩放后的长度
            val longerEdge = edgeLength * max(widthOrg, heightOrg) / min(widthOrg, heightOrg)
            // 计算压缩后的长宽
            val scaledWidth = if (widthOrg > heightOrg) {
                longerEdge
            } else {
                edgeLength
            }
            val scaledHeight = if (widthOrg > heightOrg) {
                edgeLength
            } else {
                longerEdge
            }
            val scaledBitmap: Bitmap
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
            } catch (e: Exception) {
                return bitmap
            }

            // 计算剪裁偏移量
            val xTopLeft = (scaledWidth - edgeLength) / 2
            val yTopLeft = (scaledHeight - edgeLength) / 2

            try {
                result =
                    Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength)
                scaledBitmap.recycle()
            } catch (e: Exception) {
                return bitmap
            }
        }

        return result
    }
}