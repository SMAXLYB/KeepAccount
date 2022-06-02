package life.chenshi.keepaccounts.library.view.guide

import android.content.Context
import android.graphics.Canvas
import android.graphics.Region
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.doOnPreDraw

/**
 * @description 新手引导view
 * @author lyb
 */
class GuideView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defAttr: Int = 0,
    defRes: Int = 0,
) : FrameLayout(context, attr, defAttr, defRes) {

    private var rootViewToAttach: ViewGroup? = null

    // 步骤参数， 可以有多个引导步骤
    private val guideParameters = mutableListOf<GuideParameter>()
    private var currentParameter: GuideParameter? = null

    // 默认拦截返回键
    private var interceptBackPressed = true
    private var backPressedCallBack: (() -> Unit)? = null

    // 除了高亮区域之外的遮罩背景颜色
    private val bgColor = "#a0000000".toColorInt()

    init {
        setWillNotDraw(false)
        //three line below enable maskContainer focusable and got focused
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        // 先剪裁掉不想绘制的区域
        currentParameter?.highlightShape?.path?.let {
            canvas.clipPath(it, Region.Op.DIFFERENCE)
        }

        // 绘制背景
        canvas.drawColor(bgColor)

        // 绘制高亮形状路径
        currentParameter?.highlightShape?.draw(canvas)

        canvas.restore()

        currentParameter = null
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && interceptBackPressed) {
            backPressedCallBack?.invoke()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun addGuideParameter(block: GuideParameter.() -> Unit): GuideView {
        this.guideParameters.add(GuideParameter().apply(block))
        return this
    }

    /**
     * 只允许frameLayout, 这样动态添加的时候才会显示在最上层
     */
    fun setRootView(rootView: FrameLayout): GuideView {
        this.rootViewToAttach = rootView
        return this
    }

    fun setInterceptBackPressed(allow: Boolean): GuideView {
        interceptBackPressed = allow
        return this
    }

    private fun setOnBackPressedCallBack(block: () -> Unit) {
        backPressedCallBack = block
    }

    fun show() {
        if (rootViewToAttach == null) {
            Log.e(TAG, "没有设置rootView, 请先调用setRootView")
            return
        }

        if (rootViewToAttach?.isAttachedToWindow == true || rootViewToAttach?.measuredWidth != 0) {
            if (this.parent == null) {
                rootViewToAttach?.addView(
                    this,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                )
                if (interceptBackPressed) {
                    setOnBackPressedCallBack {
                        showHighlightView()
                    }
                }

                showHighlightView()
            }
        } else {
            // 如果rootView还没被展示出来, 那么延迟绘制
            Log.w(TAG, "rootView还未展示, 延迟绘制")
            rootViewToAttach?.doOnPreDraw {
                Log.d(TAG, "延迟绘制开始")
                show()
            }
        }
    }

    private fun showHighlightView() {

        this.setOnClickListener {
            showHighlightView()
        }

        if (guideParameters.isEmpty()) {
            Log.d(TAG, "引导步骤为空, 结束引导")
            dismiss()
            return
        }

        rootViewToAttach?.let { guideParameters[0].initParameter(it) }

        showGuide(guideParameters[0])

        guideParameters.removeAt(0)
    }

    private fun showGuide(param: GuideParameter) {
        removeAllViews()
        currentParameter = param
        param.tipsView?.let {
            addView(it)
        }
    }

    private fun dismiss() {
        isFocusable = false
        isFocusableInTouchMode = false
        clearFocus()
        rootViewToAttach?.removeView(this)
        this.removeAllViews()
    }

    companion object {
        const val TAG = "GuideView"
    }
}