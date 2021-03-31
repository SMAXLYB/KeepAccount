package life.chenshi.keepaccounts.common.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.sp2px

class TextImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    // 画笔
    private val mBgPaint = Paint()
    private val mTextPaint = Paint()
    private val mBitmapPaint = Paint()

    // xml自定义属性
    private var mRadius = 0f
    private var mText: String? = null
    private var mSrc: Bitmap? = null
    private var mDrawable: Drawable? = null
    private var mBgColor = Color.WHITE
    private var mTextColor = Color.BLACK
    private var mTextSize = 14.sp2px()

    // 控件属性
    private var mHeight = 0f
    private var mWidth = 0f
    private var bitmapShader: BitmapShader? = null
    private var rect = Rect()

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TextImageView)
        mText = typedArray.getString(R.styleable.TextImageView_android_text)
        mRadius = typedArray.getDimension(R.styleable.TextImageView_android_radius, 0f)
        mSrc = AppCompatResources.getDrawable(context, typedArray.getResourceId(R.styleable.TextImageView_android_src, 0))?.toBitmap()
        mDrawable = typedArray.getDrawable(R.styleable.TextImageView_android_src)
        mBgColor = typedArray.getColor(R.styleable.TextImageView_backgroundColor, Color.WHITE)
        mTextColor = typedArray.getColor(R.styleable.TextImageView_android_textColor, Color.BLACK)
        mTextSize = typedArray.getDimension(R.styleable.TextImageView_android_textSize, 14.sp2px())
        typedArray.recycle()

        initPaint()
    }

    private fun initPaint() {

        mBgPaint.apply {
            isAntiAlias = true
            isDither = true
            color = mBgColor
        }

        mTextPaint.apply {
            isAntiAlias = true
            isDither = true
            color = mTextColor
            textSize = mTextSize
            textAlign = Paint.Align.RIGHT
        }

        mBitmapPaint.apply {
            isAntiAlias = true
            isDither = true
        }

        // src不能为空
        mSrc?.let {
            bitmapShader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val picW = mSrc?.width ?: 0
        val picH = mSrc?.height ?: 0
        val finalW = resolveSize(picW, widthMeasureSpec)
        val finalH = resolveSize(picH, heightMeasureSpec)
        setMeasuredDimension(finalW, finalH)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h.toFloat()
        mWidth = w.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        background = null
        // 先画背景
        canvas.drawRoundRect(0f, 0f, mWidth, mHeight, mRadius, mRadius, mBgPaint)

        // 文字优先级大于图片,优先显示文字
        mText?.drawText(canvas) ?: mSrc?.drawPic(canvas)
    }

    private fun Bitmap.drawPic(canvas: Canvas) {
        val matrix = Matrix()
        val scale = this@TextImageView.width.toFloat() / this.width
        matrix.setScale(scale, scale)
        bitmapShader?.setLocalMatrix(matrix)
        mBitmapPaint.shader = bitmapShader
        canvas.drawRoundRect(0f, 0f, mWidth, mHeight, mRadius, mRadius, mBitmapPaint)
    }

    private fun String.drawText(canvas: Canvas) {
        // 测量文字
        mTextPaint.getTextBounds(this, 0, this.length, rect)
        // 文字descent
        val descent = mTextPaint.fontMetrics.descent
        val w = rect.left - rect.right
        val h = rect.bottom - rect.top
        canvas.drawText(this, (mWidth - w) / 2, (mHeight + h - descent) / 2, mTextPaint)
    }

    fun setSrc(bitmap: Bitmap) {
        this.mSrc = bitmap
        bitmapShader = BitmapShader(mSrc!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        invalidate()
    }

    fun setText(text: String) {
        if (text.isBlank()) {
            return
        }

        mText = text

        // 只取前2位
        if (text.length > 2) {
            mText = text.substring(0, 2)
        }
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        mBgPaint.color = color
        invalidate()
    }
}