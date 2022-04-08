package life.chenshi.keepaccounts.module.setting.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.google.android.material.transition.MaterialContainerTransform
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.CURRENT_THEME
import life.chenshi.keepaccounts.module.common.constant.Theme
import life.chenshi.keepaccounts.module.common.utils.getColorFromAttr
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentThemeSettingBinding
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel

class ThemeSettingFragment : NavBindingFragment<SettingFragmentThemeSettingBinding>() {

    private val mViewModel by activityViewModels<AllSettingViewModel>()

    override fun setLayoutId() = R.layout.setting_fragment_theme_setting


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.setting_nav_host_fragment_container
            duration = resources.getInteger(R.integer.common_duration).toLong()
            // 过度时除了共享元素之外的蒙版颜色
            scrimColor = Color.TRANSPARENT
            // 渐变时共享元素容器变化的颜色
            setAllContainerColors(requireContext().getColorFromAttr(android.R.attr.colorBackground))
        }
    }

    override fun initView() {
        mViewModel.title.value = "主题风格"
    }

    override fun initListener() {
        binding.pink.switcher.setOnCheckChangedListener {
            KVStoreHelper.write(CURRENT_THEME, Theme.Pink.name)
        }
        binding.blue.switcher.setOnCheckChangedListener {
            KVStoreHelper.write(CURRENT_THEME, Theme.Default.name)
        }
    }

    override fun initObserver() {

    }
}