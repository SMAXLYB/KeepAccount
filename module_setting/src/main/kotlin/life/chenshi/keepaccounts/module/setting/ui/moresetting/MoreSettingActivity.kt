package life.chenshi.keepaccounts.module.setting.ui.moresetting

import life.chenshi.keepaccounts.module.common.base.BaseActivity
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingActivityMoreSettingBinding

class MoreSettingActivity : BaseActivity() {

    private val mBinding by bindingContentView<SettingActivityMoreSettingBinding>(R.layout.setting_activity_more_setting)

    override fun initView() {
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