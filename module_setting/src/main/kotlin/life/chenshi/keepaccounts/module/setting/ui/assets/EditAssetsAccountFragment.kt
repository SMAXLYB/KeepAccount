package life.chenshi.keepaccounts.module.setting.ui.assets

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import com.jeremyliao.liveeventbus.LiveEventBus
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.library.view.guide.CircleShape
import life.chenshi.keepaccounts.library.view.guide.Constraint
import life.chenshi.keepaccounts.library.view.guide.GuideView
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.ASSET_ICON
import life.chenshi.keepaccounts.module.common.constant.CURRENT_ASSET_ACCOUNT_ID
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter.Companion.TRANSITION_NAME_BACKGROUND
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter.Companion.TRANSITION_NAME_LOGO
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter.Companion.TRANSITION_NAME_TITLE
import life.chenshi.keepaccounts.module.setting.bean.assets.IAssetIcon
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentEditAssetsAccountBinding
import life.chenshi.keepaccounts.module.setting.util.AssetIconManager
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
        } ?: kotlin.run {
            GuideView(requireActivity())
                .setRootView(requireActivity().window.decorView as FrameLayout)
                .addGuideParameter {
                    setHighlight {
                        viewId = R.id.siv_assets_logo
                        paddingVertical = 4f
                        paddingHorizontal = 4f
                        shape = CircleShape(6f)
                    }
                    setTip {
                        view = TextView(requireActivity()).apply {
                            text = "点击这里切换图标"
                            setTextColor(Color.WHITE)
                            setTextSize(
                                TypedValue.COMPLEX_UNIT_PX,
                                requireContext().resources.getDimension(R.dimen.common_text_title)
                            )
                        }
                        constraints {
                            Constraint.StartToEndOfHighlight(20f) + Constraint.BottomToBottomOfHighlight(20f)
                        }
                    }
                }
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createSharedElementTransition(200L)
        sharedElementReturnTransition = createSharedElementTransition(200L)
    }

    private fun createSharedElementTransition(time: Long): Transition {
        return transitionTogether {
            duration = time
            interpolator = FAST_OUT_SLOW_IN
            this += ChangeBounds()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 延迟播放动画, 等数据加载好
        // postponeEnterTransition(100L, TimeUnit.MILLISECONDS)

        ViewCompat.setTransitionName(binding.card, TRANSITION_NAME_BACKGROUND)
        ViewCompat.setTransitionName(binding.sivAssetsLogo, TRANSITION_NAME_LOGO)
        ViewCompat.setTransitionName(binding.tvAssetsName, TRANSITION_NAME_TITLE)

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

        if (mAssetsViewModel.assetType.get().isNull()) {
            ToastUtil.showShort("请选择资产类型")
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
                assetType.set(AssetIconManager.get(it.type))
                usedDefault.set(KVStoreHelper.read(CURRENT_ASSET_ACCOUNT_ID, -1L) == it.id)
            }
        }
        LiveEventBus.get(ASSET_ICON, IAssetIcon::class.java).observe(viewLifecycleOwner) {
            mAssetsViewModel.assetType.set(it)
        }
    }

    override fun initListener() {
        binding.sivAssetsLogo.setNoDoubleClickListener {
            listener {
                SelectAssetIconDialogFragment().show(childFragmentManager, this::class.simpleName)
            }
        }
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