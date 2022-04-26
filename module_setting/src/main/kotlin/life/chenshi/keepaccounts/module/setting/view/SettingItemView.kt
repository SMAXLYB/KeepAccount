package life.chenshi.keepaccounts.module.setting.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.res.use
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import life.chenshi.keepaccounts.module.common.utils.visible
import life.chenshi.keepaccounts.module.common.view.CheckChangedListener
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingLayoutSettingItemBinding

class SettingItemView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    private val mBinding by lazy {
        SettingLayoutSettingItemBinding.inflate(LayoutInflater.from(context), this, false)
    }

    private var mTitle: String? = null
    private var mDescription: String? = null
    private var mContent: String? = null

    /**
     * onlyText=0
     * onlyArrow=1
     * textWithArrow=2
     * switch=3
     */
    private var mode = 1

    init {
        this.addView(mBinding.root, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        context.obtainStyledAttributes(attributeSet, R.styleable.SettingItemView).use {
            mode = it.getInt(R.styleable.SettingItemView_setting_mode, 1)
            mTitle = it.getString(R.styleable.SettingItemView_setting_title)
            mDescription = it.getString(R.styleable.SettingItemView_setting_description)
            mContent = it.getString(R.styleable.SettingItemView_setting_content)
        }

        mBinding.tvSettingTitle.text = mTitle ?: "默认标题"
        mBinding.tvSettingDescription.text = mDescription ?: ""
        mBinding.tvSettingContent.text = mContent ?: ""

        when (mode) {
            0 -> {
                mBinding.tvSettingContent.visible()
            }
            2 -> {
                mBinding.tvSettingContent.visible()
                mBinding.arrow.visible()
            }
            3 -> {
                mBinding.switcher.visible()
            }
            else -> {
                mBinding.arrow.visible()
            }
        }
    }


    fun setSwitchChecked(checked: Boolean) {
        mBinding.switcher.isChecked = checked
    }

    fun isSwitchChecked(): Boolean {
        return mBinding.switcher.isChecked
    }

    fun setOnSwitchCheckedChangedListener(listener: CheckChangedListener) {
        mBinding.switcher.setOnCheckChangedListener {
            listener.invoke(it)
        }
    }
}


object SettingItemViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("checked")
    fun setChecked(settingItemView: SettingItemView, checked: Boolean) {
        if (settingItemView.isSwitchChecked() != checked) {
            settingItemView.setSwitchChecked(checked)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "checked")
    fun isChecked(settingItemView: SettingItemView): Boolean {
        return settingItemView.isSwitchChecked()
    }

    @JvmStatic
    @BindingAdapter("onCheckedChanged", "checkedAttrChanged", requireAll = false)
    fun setCheckedListener(
        settingItemView: SettingItemView, checkChangedListener: CheckChangedListener?,
        attrChanged: InverseBindingListener?
    ) {
        settingItemView.setOnSwitchCheckedChangedListener {
            checkChangedListener?.invoke(it)
            attrChanged?.onChange()
        }
    }
}