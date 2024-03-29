package life.chenshi.keepaccounts.module.common.view

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Checkable
import androidx.core.animation.addListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.color.MaterialColors
import life.chenshi.keepaccounts.module.common.R
import life.chenshi.keepaccounts.module.common.utils.dp2px
import life.chenshi.keepaccounts.module.common.utils.getColorFromRes

typealias CheckChangedListener = (Boolean) -> Unit

class SwitchButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes), Checkable {

    companion object {
        private const val TAG = "SwitchButton"
        private val DEFAULT_HEIGHT = 40.dp2px()
        private val DEFAULT_WIDTH = 84.dp2px()
    }

    // xml读取
    private var uncheckedBackgroundColor = context.getColorFromRes(R.color.common_list_view_pressed_gray)
    private var uncheckedCircleColor = context.getColorFromRes(R.color.common_white)
    private var checkedBackgroundColor = MaterialColors.getColor(context, R.attr.colorPrimary, "未找到主题色")

    // private var checkedCircleColor = resources.getColor(R.color.white, null)
    private var strokeWidth: Float = 0f

    // 画笔
    private lateinit var mBackgroundPaint: Paint
    private lateinit var mCirclePaint: Paint

    // 当前变量
    private var currentBackgroundColor = 0
    private var mCurrentXPositionRatio = 0f
    private var bgRadius = 0f
    private var radius = 0f
    private var distance = 0f
    private var mChecked = false
    private var mCheckChangedListener: CheckChangedListener? = null

    // 初始化
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
        uncheckedBackgroundColor = typedArray.getColor(
            R.styleable.SwitchButton_uncheckedBackgroundColor,
            uncheckedBackgroundColor
        )
        uncheckedCircleColor =
            typedArray.getColor(R.styleable.SwitchButton_uncheckedCircleColor, uncheckedCircleColor)
        checkedBackgroundColor = typedArray.getColor(
            R.styleable.SwitchButton_checkedBackgroundColor,
            checkedBackgroundColor
        )
        // checkedCircleColor = typedArray.getColor(R.styleable.SwitchButton_checkedCircleColor, checkedCircleColor)
        strokeWidth = typedArray.getDimension(R.styleable.SwitchButton_strokeWidth, 0f)
        typedArray.recycle()

        initData()
    }

    // 初始化画笔
    private fun initData() {
        currentBackgroundColor = uncheckedBackgroundColor

        mBackgroundPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = currentBackgroundColor
        }

        mCirclePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = uncheckedCircleColor
        }

        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    // 测量
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = resolveSize(DEFAULT_WIDTH, widthMeasureSpec)
        val heightSize = resolveSize(DEFAULT_HEIGHT, heightMeasureSpec)

        if (widthSize < heightSize) {
            throw IllegalArgumentException("高度不可以大于宽度,请重新设置")
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // 水平竖直方向的Padding总和
        val paddingWidth = paddingLeft + paddingRight
        val paddingHeight = paddingTop + paddingBottom

        // 背景宽高
        val bgWidth = w - paddingWidth
        val bgHeight = h - paddingHeight

        bgRadius = bgHeight.toFloat() / 2

        if (strokeWidth == 0f) {
            strokeWidth = bgRadius / 6
        }
        radius = bgRadius - strokeWidth

        distance = bgWidth - strokeWidth * 2 - radius * 2
    }

    // 绘制
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 画背景
        mBackgroundPaint.color = currentBackgroundColor
        canvas.drawRoundRect(
            paddingLeft.toFloat(), paddingTop.toFloat(),
            (measuredWidth - paddingRight).toFloat(),
            (measuredHeight - paddingBottom).toFloat(),
            bgRadius,
            bgRadius,
            mBackgroundPaint
        )

        // 画圆
        val cy = paddingTop + bgRadius
        val currentXPosition =
            paddingLeft + radius + strokeWidth + distance * mCurrentXPositionRatio

        canvas.drawCircle(currentXPosition, cy, radius, mCirclePaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                toggle()
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    override fun setChecked(checked: Boolean) {
        // 值改变
        mChecked = checked
        mCheckChangedListener?.invoke(mChecked)

        // 播放动画
        // 如果当前已经变成选中状态
        val argbAnimator = if (mChecked) {
            ValueAnimator.ofInt(currentBackgroundColor, checkedBackgroundColor)
        } else {
            ValueAnimator.ofInt(currentBackgroundColor, uncheckedBackgroundColor)
        }

        val transAnimator = if (mChecked) {
            ObjectAnimator.ofFloat(this, "currentXPositionRatio", 0f, 1f)
        } else {
            ObjectAnimator.ofFloat(this, "currentXPositionRatio", 1f, 0f)
        }
        argbAnimator.setEvaluator(ArgbEvaluator())
        argbAnimator.addUpdateListener {
            val color = it.animatedValue as Int
            currentBackgroundColor = color
            invalidate()
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(argbAnimator, transAnimator)
        animatorSet.interpolator = AccelerateInterpolator(2f)
        animatorSet.duration = 200
        animatorSet.addListener(
            onStart = { isClickable = false },
            onEnd = {
                isClickable = true
            }
        )
        animatorSet.start()
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun toggle() {
        isChecked = !mChecked
    }

    fun setOnCheckChangedListener(listener: CheckChangedListener) {
        this.mCheckChangedListener = listener
    }

    fun setCurrentXPositionRatio(arg: Float) {
        this.mCurrentXPositionRatio = arg
    }

    /**
     * 重置为选中状态, 此时不会立即重绘, 适合第一次初始化的时候用
     */
    fun resetToChecked() {
        if (mChecked) {
            return
        }
        currentBackgroundColor = checkedBackgroundColor
        mCurrentXPositionRatio = 1f
        mChecked = !mChecked
    }

    /**
     * 重置为非选中状态, 此时不会立即重绘, 适合第一次初始化的时候用
     */
    fun resetToUnchecked() {
        if (!mChecked) {
            return
        }
        currentBackgroundColor = uncheckedBackgroundColor
        mCurrentXPositionRatio = 0f
        mChecked = !mChecked
    }

    /**
     * 此时不会立即重绘, 适合第一次初始化的时候用
     */
    fun reset(checked: Boolean) {
        if (checked) {
            resetToChecked()
        } else {
            resetToUnchecked()
        }
    }
}

// 要么用InverseBindingMethods, 要么用InverseBindingAdapter, 反正要告诉dataBinding怎么调get方法
// @InverseBindingMethods(InverseBindingMethod(type = SwitchButton::class, attribute = "checked"))
object SwitchButtonBindingAdapter {

    /**
     * 双向绑定, 防止循环
     */
    @JvmStatic
    @BindingAdapter("checked")
    fun setChecked(switcher: SwitchButton, checked: Boolean) {
        if (switcher.isChecked != checked) {
            switcher.isChecked = checked
        }
    }

    /**
     * 这里可以根据情况改变返回值
     */
    @JvmStatic
    @InverseBindingAdapter(attribute = "checked")
    fun isChecked(switcher: SwitchButton): Boolean {
        return switcher.isChecked
    }

    /**
     * 设置监听, 告诉view在什么时候触发dataBinding的数据应该更新的回调
     */
    @JvmStatic
    @BindingAdapter("onCheckedChanged", "checkedAttrChanged", requireAll = false)
    fun setListeners(
        switcher: SwitchButton,
        onCheckedChangedListener: CheckChangedListener?,
        attrChangedListener: InverseBindingListener?
    ) {
        switcher.setOnCheckChangedListener {
            onCheckedChangedListener?.invoke(it)
            attrChangedListener?.onChange()
        }
    }
}
