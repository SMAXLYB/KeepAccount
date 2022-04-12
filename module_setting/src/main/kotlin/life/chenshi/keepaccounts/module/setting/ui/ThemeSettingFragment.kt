package life.chenshi.keepaccounts.module.setting.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.CURRENT_THEME
import life.chenshi.keepaccounts.module.common.constant.Theme
import life.chenshi.keepaccounts.module.common.utils.ActivityStackManager
import life.chenshi.keepaccounts.module.common.utils.getColorFromAttr
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.adapter.ThemeAdapter
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentThemeSettingBinding
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel

class ThemeSettingFragment : NavBindingFragment<SettingFragmentThemeSettingBinding>() {

    private val mViewModel by activityViewModels<AllSettingViewModel>()
    private val mAdapter = ThemeAdapter(listOf(Theme.Default, Theme.Pink, Theme.Green))

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
        binding.rvTheme.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initListener() {
        mAdapter.setOnItemClickListener { _, theme, _ ->
            val currentTheme = Theme.getThemeFromName(KVStoreHelper.read(CURRENT_THEME, Theme.Default.name))
            if (currentTheme == theme) {
                return@setOnItemClickListener
            }
            KVStoreHelper.write(CURRENT_THEME, theme.name)
            mAdapter.notifyDataSetChanged()
            ActivityStackManager.activityStack.forEach {
                if (it !is MainActivity) {
                    ActivityCompat.recreate(it)
                }
            }
        }
    }

    override fun initObserver() {

    }
}