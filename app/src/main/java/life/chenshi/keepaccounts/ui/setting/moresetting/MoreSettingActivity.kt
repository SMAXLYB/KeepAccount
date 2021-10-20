package life.chenshi.keepaccounts.ui.setting.moresetting

import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.ActivityMoreSettingBinding
import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil

class MoreSettingActivity : BaseActivity() {

    private val mBinding by bindingContentView<ActivityMoreSettingBinding>(R.layout.activity_more_setting)

    override fun initView() {
        mBinding
        StatusBarUtil.init(this)
            .setColor(R.color.white)
            .setDarkMode(true)

        mBinding.bar.apply {
            setLeftClickListener {
                onBackPressed()
            }
        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
    }
}