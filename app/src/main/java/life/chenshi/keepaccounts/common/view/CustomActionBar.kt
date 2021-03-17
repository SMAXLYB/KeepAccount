package life.chenshi.keepaccounts.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.common.utils.setVisibilityWithText
import life.chenshi.keepaccounts.databinding.LayoutActionBarBinding

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

    init {
        this.addView(mBinding.root, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.CustomActionBar)
        mLeftTitle = typedArray.getString(R.styleable.CustomActionBar_leftTitle)
        mCenterTitle = typedArray.getString(R.styleable.CustomActionBar_centerTitle)
        mRightTitle = typedArray.getString(R.styleable.CustomActionBar_rightTitle)
        typedArray.recycle()

        setLeftTitle(mLeftTitle ?: "")
        setCenterTitle(mCenterTitle ?: "")
        setRightTitle(mRightTitle ?: "")
    }

    /**
     * 设置左边标题
     */
    fun setLeftTitle(title: String) {
        mBinding.tvLeft.setVisibilityWithText(title)
    }

    /**
     * 设置中间标题
     */
    fun setCenterTitle(title: String) {
        mBinding.tvCenter.setVisibilityWithText(title)
    }

    /**
     * 设置右边标题
     */
    fun setRightTitle(title: String) {
        mBinding.tvRight.setVisibilityWithText(title)
    }

    /**
     * 设置中间icon, 前提是中间标题有内容, 否则不显示
     */
    fun setCenterIcon() {

    }


    fun setLeftClickListener() {

    }

    fun setCenterClickListener() {}

    fun setRightClickListener() {

    }
}