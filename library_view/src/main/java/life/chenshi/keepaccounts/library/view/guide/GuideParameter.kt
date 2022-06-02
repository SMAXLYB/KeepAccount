package life.chenshi.keepaccounts.library.view.guide

import android.graphics.RectF
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

class GuideParameter {

    // 高亮view的id
    @IdRes
    internal var highlightViewId: Int = -1

    // 高亮view
    private var highlightView: View? = null

    // 引导文案布局的id
    @LayoutRes
    internal var tipsViewId: Int = -1

    // 引导文案view
    internal var tipsView: View? = null

    // 高亮view和遮罩的间距，竖直和水平两个方向
    private var horPaddingToHighlightView = 0f
    private var verPaddingToHighlightView = 0f

    internal var highlightShape: HighlightShape = RectShape(30f, 30f, 5f)

    // 引导文案默认和在highlight下方
    private var constraints = listOf(
        Constraint.TopToBottomOfHighlight(), Constraint.StartToStartOfHighlight()
    )

    // 高亮的区域范围
    private var highlightRect = RectF()

    fun setHighlight(highlightParameter: HighlightParameter.() -> Unit) {
        val parameter = HighlightParameter().apply(highlightParameter)
        this.highlightViewId = parameter.viewId
        this.highlightView = parameter.view
        this.horPaddingToHighlightView = parameter.paddingHorizontal
        this.verPaddingToHighlightView = parameter.paddingVertical
        parameter.shape?.let {
            this.highlightShape = it
        }
    }

    fun setTip(tipParameter: TipParameter.() -> Unit) {
        val parameter = TipParameter().apply(tipParameter)
        this.tipsViewId = parameter.viewId
        this.tipsView = parameter.view
        parameter.constraintsBlock().apply {
            if (isNotEmpty()) {
                constraints = this
            }
        }
    }

    internal fun initParameter(rootView: ViewGroup) {
        if (highlightView == null) {
            highlightView = rootView.findViewById(highlightViewId)
        }
        if (tipsView == null) {
            try {
                tipsView = LayoutInflater.from(rootView.context).inflate(tipsViewId, rootView, false)
            } catch (e: Exception) {
                Log.e(GuideView.TAG, "引导文案view初始化失败")
            }
        }
        highlightView?.let {
            // 拿到高亮view在屏幕上的位置
            val rect = it.getRectOnScreen()
            // 拿到根布局在屏幕上的左上角位置
            val rootViewPos = IntArray(2)
            rootView.getLocationOnScreen(rootViewPos)
            // 如果rootView不是decorView，那么要重新计算高亮遮罩的位置
            rect.left -= (rootViewPos[0] + rootView.paddingLeft + horPaddingToHighlightView)
            rect.right -= (rootViewPos[0] + rootView.paddingLeft - verPaddingToHighlightView)
            rect.top -= (rootViewPos[1] + rootView.paddingTop + horPaddingToHighlightView)
            rect.bottom -= (rootViewPos[1] + rootView.paddingTop - verPaddingToHighlightView)
            highlightRect = rect
            highlightShape.setRect(highlightRect)
        }
        tipsView?.let {
            val layoutParams = (it.layoutParams ?: FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
            )) as FrameLayout.LayoutParams

            val gravities = mutableListOf<Int>()
            val rootWidth = rootView.width - rootView.paddingLeft - rootView.paddingRight
            val rootHeight = rootView.height - rootView.paddingTop - rootView.paddingBottom

            constraints.forEach { constraint ->
                when (constraint) {
                    is Constraint.StartToStartOfHighlight -> {
                        layoutParams.leftMargin = (highlightRect.left + constraint.offset).toInt()
                        gravities.add(Gravity.START)
                    }

                    is Constraint.EndToStartOfHighlight -> {
                        // 从右边开始算
                        layoutParams.rightMargin =
                            (rootWidth - highlightRect.right + highlightRect.width() + constraint.offset).toInt()
                        gravities.add(Gravity.END)
                    }

                    is Constraint.StartToEndOfHighlight -> {
                        layoutParams.leftMargin = (highlightRect.right + constraint.offset).toInt()
                        gravities.add(Gravity.START)
                    }

                    is Constraint.EndToEndOfHighlight -> {
                        layoutParams.rightMargin =
                            (rootWidth - highlightRect.right + constraint.offset).toInt()
                        gravities.add(Gravity.END)
                    }

                    is Constraint.TopToTopOfHighlight -> {
                        layoutParams.topMargin = (highlightRect.top + constraint.offset).toInt()
                        gravities.add(Gravity.TOP)
                    }

                    is Constraint.BottomToBottomOfHighlight -> {
                        layoutParams.bottomMargin =
                            (rootHeight - highlightRect.bottom + constraint.offset).toInt()
                        gravities.add(Gravity.BOTTOM)

                    }

                    is Constraint.BottomToTopOfHighlight -> {
                        layoutParams.bottomMargin =
                            (rootHeight - highlightRect.bottom + highlightRect.height() + constraint.offset).toInt()
                        gravities.add(Gravity.BOTTOM)
                    }

                    is Constraint.TopToBottomOfHighlight -> {
                        layoutParams.topMargin = (highlightRect.bottom + constraint.offset).toInt()
                        gravities.add(Gravity.TOP)
                    }
                }
            }

            gravities.forEachIndexed { index, gravity ->
                if (index == 0) layoutParams.gravity = gravity
                else layoutParams.gravity = layoutParams.gravity or gravity
            }

            it.layoutParams = layoutParams
        }
    }

    class HighlightParameter() {
        // 高亮view的id
        @IdRes
        var viewId: Int = -1

        // 高亮view
        var view: View? = null

        var paddingVertical = 0f
        var paddingHorizontal = 0f

        var shape: HighlightShape? = null
    }

    class TipParameter() {
        // 引导文案布局的id
        @LayoutRes
        var viewId: Int = -1

        // 引导文案view
        var view: View? = null

        internal var constraintsBlock: () -> List<Constraint> = { emptyList() }

        fun constraints(block: () -> List<Constraint>) {
            constraintsBlock = block
        }
    }
}

fun View?.getRectOnScreen(): RectF {
    if (this == null) {
        return RectF()
    }
    val pos = IntArray(2)
    this.getLocationOnScreen(pos)
    return RectF(
        pos[0].toFloat(), pos[1].toFloat(), pos[0].toFloat() + width, pos[1].toFloat() + height
    )
}