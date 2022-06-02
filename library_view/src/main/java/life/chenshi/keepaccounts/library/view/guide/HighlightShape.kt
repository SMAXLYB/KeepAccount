package life.chenshi.keepaccounts.library.view.guide

import android.graphics.*

/**
 * @param rx x轴曲率半径
 * @param ry y轴曲率半径
 * @param blurRadius 模糊半径
 */
abstract class HighlightShape(private val blurRadius: Float) {

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

    abstract fun setRect(rect: RectF)

    fun draw(canvas: Canvas) {
        if (path.isEmpty.not()) {
            canvas.drawPath(path, paint)
        }
    }
}