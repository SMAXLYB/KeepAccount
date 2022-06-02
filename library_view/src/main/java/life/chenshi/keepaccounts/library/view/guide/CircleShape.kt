package life.chenshi.keepaccounts.library.view.guide

import android.graphics.Path
import android.graphics.RectF
import java.lang.Float.max

class CircleShape(private val radius: Float = 0f, blurRadius: Float = 0f) : HighlightShape(blurRadius) {

    override fun setRect(rect: RectF) {
        path.reset()
        rect.apply {
            path.addCircle((left + right) / 2, (top + bottom) / 2, max(height(), width()) / 2, Path.Direction.CW)
        }
    }
}