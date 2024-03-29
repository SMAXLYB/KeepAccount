package life.chenshi.keepaccounts.module.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.color.MaterialColors
import life.chenshi.keepaccounts.module.common.R
import life.chenshi.keepaccounts.module.common.utils.dp2px

class UnderLineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val mTextPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnderLineTextView)

        val color = typedArray.getColor(R.styleable.UnderLineTextView_underline_color, MaterialColors.getColor(context, R.attr.colorPrimary, "未找到主题色"))
        val strokeWidth = typedArray.getDimension(R.styleable.UnderLineTextView_underline_stroke, 10f)

        mTextPaint.apply {
            this.color = color
            this.strokeWidth = strokeWidth
        }

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawLine(
            0f + 3.dp2px() + paddingStart,
            height.toFloat() - 5.dp2px(),
            width.toFloat() - 3.dp2px() - paddingEnd,
            height.toFloat() - 5.dp2px(),
            mTextPaint
        )

        super.onDraw(canvas)
    }

}