package life.chenshi.keepaccounts.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.LayoutActionBarBinding
import life.chenshi.keepaccounts.module.common.utils.*

class CustomActionBar @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attr, defStyleAttr, defStyleRes) {

    private val mBinding by lazy {
        LayoutActionBarBinding.inflate(LayoutInflater.from(context), this, false)
    }
    private var mLeftTitle: String? = null
    private var mCenterTitle: String? = null
    private var mRightTitle: String? = null
    private var mLeftIcon: Drawable? = null
    private var mCenterIcon: Drawable? = null
    private var mRightIcon: Drawable? = null

    init {
        this.addView(mBinding.root, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.CustomActionBar)
        mLeftTitle = typedArray.getString(R.styleable.CustomActionBar_leftTitle)
        mCenterTitle = typedArray.getString(R.styleable.CustomActionBar_centerTitle)
        mRightTitle = typedArray.getString(R.styleable.CustomActionBar_rightTitle)
        typedArray.getResourceId(R.styleable.CustomActionBar_leftIcon, 0).takeIf { it != 0 }?.apply {
            mLeftIcon = AppCompatResources.getDrawable(context, this)
        }
        typedArray.getResourceId(R.styleable.CustomActionBar_centerIcon, 0).takeIf { it != 0 }?.apply {
            mCenterIcon = AppCompatResources.getDrawable(context, this)
        }
        typedArray.getResourceId(R.styleable.CustomActionBar_rightIcon, 0).takeIf { it != 0 }?.apply {
            mRightIcon = AppCompatResources.getDrawable(context, this)
        }
        typedArray.recycle()

        setLeftTitle(mLeftTitle ?: "")
        setCenterTitle(mCenterTitle ?: "")
        setRightTitle(mRightTitle ?: "")

        mLeftIcon?.apply { setLeftIcon(this) }
        mCenterIcon?.apply { setCenterIcon(this) }
        mRightIcon?.apply { setRightIcon(this) }
    }

    /**
     * 设置左边标题
     */
    fun setLeftTitle(title: String) {
        mBinding.tvLeft.setVisibilityWithText(title)
    }

    fun hideLeftTitle() {
        mBinding.tvLeft.inVisible()
    }

    fun showLeftTitle() {
        mBinding.tvLeft.visible()
    }

    /**
     * 设置中间标题
     */
    fun setCenterTitle(title: String) {
        mBinding.tvCenter.setVisibilityWithText(title)
    }

    fun hideCenterTitle() {
        mBinding.tvCenter.inVisible()
    }

    fun showCenterTitle() {
        mBinding.tvCenter.visible()
    }

    /**
     * 设置右边标题
     */
    fun setRightTitle(title: String) {
        mBinding.tvRight.setVisibilityWithText(title)
    }

    fun hideRightTitle() {
        mBinding.tvRight.inVisible()
    }

    fun showRightTitle() {
        mBinding.tvRight.visible()
    }

    /**
     * 设置中间icon, 前提是中间标题有内容, 否则不显示
     */
    fun setCenterIcon(drawable: Drawable) {
        mBinding.tvCenter.takeIf {
            it.isVisible
        }?.run {
            mBinding.ivCenter.setVisibleWithDrawable(drawable)
        }
    }

    fun hideCenterIcon() {
        mBinding.ivCenter.inVisible()
    }

    fun showCenterIcon() {
        mBinding.tvCenter.takeIf {
            it.isVisible
        }?.run {
            mBinding.ivCenter.visible()
        }
    }

    /**
     * 设置左边icon
     */
    fun hideLeftIcon() {
        mBinding.ivLeft.gone()
    }

    fun showLeftIcon() {
        mBinding.ivLeft.visible()
    }

    fun setLeftIcon(drawable: Drawable) {
        mBinding.ivLeft.setVisibleWithDrawable(drawable)
    }


    /**
     * 设置右边icon
     */
    fun setRightIcon(drawable: Drawable) {
        mBinding.ivRight.setVisibleWithDrawable(drawable)
    }

    fun hideRightIcon() {
        mBinding.ivRight.gone()
    }

    fun showRightIcon() {
        mBinding.ivRight.visible()
    }

    /**
     * 设置监听
     */
    fun setLeftClickListener(listener: (View) -> Unit) {
        mBinding.tvLeft.setOnClickListener {
            listener.invoke(it)
        }
        mBinding.ivLeft.setOnClickListener {
            listener.invoke(it)
        }
    }

    fun setCenterClickListener(listener: (View) -> Unit) {
        mBinding.tvCenter.setOnClickListener {
            listener.invoke(it)

        }

        mBinding.ivCenter.setOnClickListener {
            listener.invoke(it)

        }
    }

    fun setRightClickListener(listener: (View) -> Unit) {
        mBinding.tvRight.setOnClickListener {
            listener.invoke(it)

        }
        mBinding.ivRight.setOnClickListener {
            listener.invoke(it)

        }
    }

    fun setRightTitleClickListener(listener: (View) -> Unit){
        mBinding.tvRight.setOnClickListener {
            listener.invoke(it)
        }
    }
    fun setRightIconClickListener(listener: (View) -> Unit) {
        mBinding.ivRight.setOnClickListener {
            listener.invoke(it)

        }
    }
}