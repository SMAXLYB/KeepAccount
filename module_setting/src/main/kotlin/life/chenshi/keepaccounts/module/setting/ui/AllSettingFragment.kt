package life.chenshi.keepaccounts.module.setting.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.fragment.app.activityViewModels
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CLOSE_AD
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_EXIT_APP
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentAllSettingBinding
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel

@SuppressLint("SetTextI18n")
class AllSettingFragment : NavBindingFragment<SettingFragmentAllSettingBinding>() {

    private val mViewModel by activityViewModels<AllSettingViewModel>()

    override fun setLayoutId() = R.layout.setting_fragment_all_setting

    override fun onResume() {
        super.onResume()
        mViewModel.title.value = "全部设置"
    }

    override fun initView() {
        with(KVStoreHelper.read(SWITCHER_EXIT_APP, true)) {
            binding.settingExit.setSwitchChecked(this)
        }
        with(KVStoreHelper.read(SWITCHER_CONFIRM_BEFORE_DELETE, true)) {
            binding.settingDelete.setSwitchChecked(this)
        }
        with(KVStoreHelper.read(SWITCHER_CLOSE_AD, false)) {
            binding.settingCloseAd.setSwitchChecked(this)
        }
        context?.let {
            try {
                binding.tvSettingCurrentVersion.text = "当前版本: " +
                        it.packageManager.getPackageInfo(it.packageName, PackageManager.GET_CONFIGURATIONS).versionName
            } catch (e: Exception) {
                binding.tvSettingCurrentVersion.text = "当前版本: 获取失败"
            }
        }

    }

    override fun initListener() {
        binding.settingExit.setOnSwitchCheckedChangedListener {
            KVStoreHelper.write(SWITCHER_EXIT_APP, it)
        }
        binding.settingDelete.setOnSwitchCheckedChangedListener {
            KVStoreHelper.write(SWITCHER_CONFIRM_BEFORE_DELETE, it)
        }
        binding.settingCloseAd.setOnSwitchCheckedChangedListener {
            KVStoreHelper.write(SWITCHER_CLOSE_AD, it)
        }
    }
}
