package life.chenshi.keepaccounts.module.setting.ui

import android.database.sqlite.SQLiteConstraintException
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.utils.ToastUtil
import life.chenshi.keepaccounts.module.common.utils.getColorFromAttr
import life.chenshi.keepaccounts.module.common.utils.navController
import life.chenshi.keepaccounts.module.common.utils.setNoDoubleClickListener
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentEditAssetsAccountBinding
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel
import life.chenshi.keepaccounts.module.setting.vm.EditAssetsAccountViewModel

@AndroidEntryPoint
class EditAssetsAccountFragment : NavBindingFragment<SettingFragmentEditAssetsAccountBinding>() {

    private val mAssetsViewModel: EditAssetsAccountViewModel by viewModels()
    private val mSettingViewModel: AllSettingViewModel by activityViewModels()
    private val args by navArgs<EditAssetsAccountFragmentArgs>()

    override fun setLayoutId() = R.layout.setting_fragment_edit_assets_account

    override fun initView() {
        binding.viewModel = mAssetsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        args.assetsAccount?.let {
            mAssetsViewModel.assetsAccount.value = it
        }
    }

    override fun onResume() {
        super.onResume()
        mSettingViewModel.rightIconAction.value = getDrawable() to { insertAssetsAccount() }
    }

    private fun getDrawable(): Drawable {
        return AppCompatResources.getDrawable(requireContext(), R.drawable.common_action_bar_complete)!!
    }

    private fun insertAssetsAccount() {
        if (!dataVerification()) {
            return
        }
        viewLifecycleOwner.lifecycleScope.launch {
            kotlin.runCatching {
                mAssetsViewModel.insertOrUpdateAssetsAccount()
            }.onSuccess {
                ToastUtil.showSuccess("保存成功")
                navController?.navigateUp()
            }.onFailure {
                when (it) {
                    is SQLiteConstraintException -> {
                        ToastUtil.showFail("账户名称重复, 换一个吧~")
                    }
                    is NumberFormatException -> {
                        ToastUtil.showFail("金额不符合规范, 请检查")
                    }
                    else -> {
                        ToastUtil.showFail("添加失败, 发生未知错误")
                    }
                }
            }
        }

    }

    private fun dataVerification(): Boolean {
        if (mAssetsViewModel.assetName.get().isNullOrBlank()) {
            ToastUtil.showShort("资产名称不能为空")
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mSettingViewModel.rightIconAction.value = null
    }

    override fun initObserver() {
        // 进来之后只观察一次，后续切到别的fragment再切回来，不要走回调
        mAssetsViewModel.assetsAccount.observe(this) {
            with(mAssetsViewModel) {
                assetName.set(it.name)
                assetNumber.set(it.number)
                assetRemark.set(it.remark)
                assetExpireDate.set(it.expireTime.time)
                assetBalance.set(it.balance.toPlainString())
                includedInAll.set(it.includedInAllAsset)
            }
        }
    }

    override fun initListener() {
        binding.tvDate.setNoDoubleClickListener {
            listener {
                CardDatePickerDialog.builder(requireContext())
                    .setBackGroundModel(CardDatePickerDialog.STACK)
                    .setThemeColor(requireContext().getColorFromAttr(R.attr.colorPrimary))
                    .setDefaultTime(mAssetsViewModel.assetExpireDate.get())
                    .showBackNow(false)
                    .setDisplayType(
                        DateTimeConfig.YEAR,
                        DateTimeConfig.MONTH,
                        DateTimeConfig.DAY,
                    )
                    .setLabelText("年", "月", "日")
                    .setOnChoose {
                        mAssetsViewModel.assetExpireDate.set(it)
                    }
                    .build()
                    .show()
            }
        }
    }
}