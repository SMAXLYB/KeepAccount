package life.chenshi.keepaccounts.module.setting.ui

import androidx.fragment.app.activityViewModels
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_EXIT_APP
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentAllSettingBinding
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel

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
    }

    override fun initListener() {
        // binding.settingTheme.root.setOnClickListener {
        //     exitTransition = MaterialElevationScale(false).apply {
        //         duration = resources.getInteger(R.integer.common_duration).toLong()
        //     }
        //     reenterTransition = MaterialElevationScale(true).apply {
        //         duration = resources.getInteger(R.integer.common_duration).toLong()
        //     }
        //
        //     val extras = FragmentNavigatorExtras(it to "allSettingToThemeSetting")
        //     val directions = AllSettingFragmentDirections.settingActionAllsettingfragmentToThemesettingfragment()
        //     navController.navigate(directions, extras)
        // }
        binding.settingExit.setOnSwitchCheckedChangedListener {
            KVStoreHelper.write(SWITCHER_EXIT_APP, it)
        }
        binding.settingDelete.setOnSwitchCheckedChangedListener {
            KVStoreHelper.write(SWITCHER_CONFIRM_BEFORE_DELETE, it)
        }
    }
}
