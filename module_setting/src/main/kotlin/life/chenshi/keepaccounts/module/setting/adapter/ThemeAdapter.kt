package life.chenshi.keepaccounts.module.setting.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import life.chenshi.keepaccounts.module.common.base.BaseAdapter
import life.chenshi.keepaccounts.module.common.constant.CURRENT_THEME
import life.chenshi.keepaccounts.module.common.constant.Theme
import life.chenshi.keepaccounts.module.common.utils.getColorFromAttr
import life.chenshi.keepaccounts.module.common.utils.getColorFromTheme
import life.chenshi.keepaccounts.module.common.utils.setShapeWithRippleBackground
import life.chenshi.keepaccounts.module.common.utils.setVisibility
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingItemThemeBinding


class ThemeAdapter(data: List<Theme>) : BaseAdapter<Theme, SettingItemThemeBinding>(data) {

    override fun onBindViewHolder(binding: SettingItemThemeBinding, itemData: Theme) {
        val currentTheme = Theme.getThemeFromName(KVStoreHelper.read(CURRENT_THEME, Theme.Default.name))
        val context = binding.clContainer.context
        val sam = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, context.resources.getDimension(R.dimen.common_medium_corner_size))
            .build()
        val clBackground = MaterialShapeDrawable(sam).apply {
            fillColor = ColorStateList.valueOf(context.getColorFromAttr(R.attr.colorSurface))
        }
        binding.ivThemeSelected.setVisibility(currentTheme == itemData)
        binding.clContainer.background = clBackground
        binding.vThemeColor.setShapeWithRippleBackground(
            context.resources.getDimension(R.dimen.common_medium_corner_size),
            context.getColorFromTheme(itemData.styRes, R.attr.colorPrimary),
            Color.parseColor("#27707070")
        )
        binding.tvThemeName.text = itemData.nameInCN
    }

    override fun setResLayoutId() = R.layout.setting_item_theme
}


