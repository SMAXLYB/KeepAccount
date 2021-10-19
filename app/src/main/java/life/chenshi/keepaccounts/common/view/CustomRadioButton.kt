package life.chenshi.keepaccounts.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.os.bundleOf
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.module.common.utils.dp2px
import life.chenshi.keepaccounts.module.common.utils.sp2px

/**
 * @description: 自定义单选按钮组合, item为字符串, 使用英文逗号分割
 * @author lyb
 * @date 2021年04月06日 20:49
 */
class CustomRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * 保存按钮字符串合集
     */
    private var mItems = listOf<String>()
    private var mItemTextColor = 0x00000000
    private var mItemTextSize = 0f
    private var mItemSelectedTextColor = 0x00000000
    private var mItemSelectedBgColor = 0x00000000
    private var mBackgroundColor = 0x00000000
    private var mRadius = 0f
    private var mItemRadius = 0f
    private val mBgPaint = Paint()
    private val mTextPaint = Paint()
    private val mItemPaint = Paint()
    private var mWidth = 0f
    private var mHeight = 0f
    private var mVerticalSpacing = 0f
    private var mHorizontalSpacing = 0f
    private var mListener: ((Int) -> Unit)? = null

    /**
     * 当前选中 0-n
     */
    private var mCurrentItem = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRadioButton)

        typedArray.getString(R.styleable.CustomRadioButton_buttonText)?.let {
            mItems = it.split(",")
        }
        mItemTextColor = typedArray.getColor(R.styleable.CustomRadioButton_buttonTextColor, Color.GRAY)
        mItemTextSize = typedArray.getDimension(R.styleable.CustomRadioButton_buttonTextSize, 14.sp2px())
        mItemSelectedTextColor = typedArray.getColor(R.styleable.CustomRadioButton_buttonSelectedTextColor, Color.WHITE)
        mItemSelectedBgColor = typedArray.getColor(R.styleable.CustomRadioButton_buttonSelectedBgColor, Color.BLACK)
        mBackgroundColor = typedArray.getColor(R.styleable.CustomRadioButton_bgColor, 0x0000000)
        mRadius = typedArray.getDimension(R.styleable.CustomRadioButton_radius, 5.dp2px().toFloat())
        mItemRadius = typedArray.getDimension(R.styleable.CustomRadioButton_buttonRadius, 3.dp2px().toFloat())
        mHorizontalSpacing = typedArray.getDimension(R.styleable.CustomRadioButton_buttonHorizontalSpacing, 2.dp2px().toFloat())
        mVerticalSpacing = typedArray.getDimension(R.styleable.CustomRadioButton_buttonVerticalSpacing, 2.dp2px().toFloat())
        typedArray.recycle()

        initPaint()
    }

    private fun initPaint() {
        mBgPaint.apply {
            isDither = true
            isAntiAlias = true
            color = mBackgroundColor
        }

        mTextPaint.apply {
            isDither = true
            isAntiAlias = true
            color = mItemTextColor
            textSize = mItemTextSize
            textAlign = Paint.Align.CENTER
        }

        mItemPaint.apply {
            isDither = true
            isAntiAlias = true
            color = mItemSelectedBgColor
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas)
        drawItem(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(paddingLeft.toFloat(), paddingTop.toFloat(), mWidth - paddingRight, mHeight - paddingBottom, mRadius, mRadius, mBgPaint)
    }

    private fun drawItem(canvas: Canvas) {
        if (mItems.isNullOrEmpty()) {
            throw IllegalArgumentException("子项不能为空")
        }

        // 去除padding后的宽高
        val totalW = mWidth - paddingLeft - paddingRight
        // 每个Item所占区域的大小
        val perW = totalW / mItems.size

        val fontMetrics = mTextPaint.fontMetrics
        val top = fontMetrics.top
        val bottom = fontMetrics.bottom

        // 绘制字体
        mItems.forEachIndexed { index, s ->
            // 如果当前是选中,绘制背景
            if (index == mCurrentItem) {
                canvas.drawRoundRect(
                    paddingLeft + mHorizontalSpacing + perW * index,
                    paddingTop + mVerticalSpacing,
                    paddingLeft + perW * (index + 1) - mHorizontalSpacing,
                    mHeight - paddingBottom - mVerticalSpacing,
                    mItemRadius,
                    mItemRadius,
                    mItemPaint
                )
                mTextPaint.color = mItemSelectedTextColor
            }

            // 如果是选中,绘制字体要改变颜色
            canvas.drawText(s, paddingLeft + perW / 2 * (2 * index + 1), (mHeight - top - bottom) / 2, mTextPaint)
            mTextPaint.color = mItemTextColor
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        // 去除padding后的宽高
        val totalW = mWidth - paddingLeft - paddingRight
        // 每个Item所占区域的大小
        val perW = totalW / mItems.size

        Log.d("TAG", "onTouchEvent: ${event.action}")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                // 如果y符合条件
                if (0 < y && y < mHeight) {
                    // 如果x符合条件
                    if (0 < x && x < mWidth) {
                        mItems.forEachIndexed { index, _ ->
                            if (x in (paddingLeft + perW * index)..(paddingLeft + perW * (index + 1))) {
                                setCurrentItem(index)
                                mListener?.invoke(index)
                                return true
                            }
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.mListener = listener
    }

    private fun setCurrentItem(position: Int) {
        mCurrentItem = position
        invalidate()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()
        return bundleOf("super" to parcelable, "current_item" to mCurrentItem)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            mCurrentItem = state.getInt("current_item")
            super.onRestoreInstanceState(state.getParcelable("super"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}