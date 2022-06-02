package life.chenshi.keepaccounts.library.view.guide

import android.graphics.Path
import android.graphics.RectF

class RectShape(private val rx: Float = 0f, private val ry: Float = 0f, blurRadius: Float = 0f) :
    HighlightShape(blurRadius) {

    override fun setRect(rect: RectF) {
        path.reset()
        path.addRoundRect(rect, rx, ry, Path.Direction.CW)
    }
}