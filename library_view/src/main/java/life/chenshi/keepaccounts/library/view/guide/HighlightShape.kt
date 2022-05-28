package life.chenshi.keepaccounts.library.view.guide

import android.graphics.*

/**
 * @param rx x轴曲率半径
 * @param ry y轴曲率半径
 * @param blurRadius 模糊半径
 */
class HighlightShape(private val rx: Float = 0f, private val ry: Float = 0f, private val blurRadius: Float = 0f) {

    private var paint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.WHITE
    }

    internal val path = Path()

    init {
        if (blurRadius > 0) {
            paint.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.SOLID)
        }
    }

    fun setRect(rect: RectF) {
        path.reset()
        path.addRoundRect(rect, rx, ry, Path.Direction.CW)
    }

    fun draw(canvas: Canvas) {
        if (path.isEmpty.not()) {
            canvas.drawPath(path, paint)
        }
    }
}