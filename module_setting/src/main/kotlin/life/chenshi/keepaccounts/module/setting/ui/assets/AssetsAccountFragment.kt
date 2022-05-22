package life.chenshi.keepaccounts.module.setting.ui.assets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.module.common.utils.navController
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter.Companion.TRANSITION_NAME_BACKGROUND
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter.Companion.TRANSITION_NAME_LOGO
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter.Companion.TRANSITION_NAME_TITLE
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountFooterAdapter
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentAssetsAccountBinding
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel
import life.chenshi.keepaccounts.module.setting.vm.AssetsAccountViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class AssetsAccountFragment : NavBindingFragment<SettingFragmentAssetsAccountBinding>() {

    private val mSettingViewModel: AllSettingViewModel by activityViewModels()
    private val mAssetsViewModel: AssetsAccountViewModel by viewModels()

    @Inject
    internal lateinit var mAdapter: AssetsAccountAdapter

    @Inject
    internal lateinit var mFooterAdapter: AssetsAccountFooterAdapter

    override fun setLayoutId() = R.layout.setting_fragment_assets_account

    override fun initView() {
        binding.rvAssetsAccountList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter(mAdapter, mFooterAdapter)
        }
    }

    override fun onResume() {
        super.onResume()
        mSettingViewModel.title.value = "资产账户"
    }

    override fun initObserver() {
        mAssetsViewModel.allAssetsAccount.observe(viewLifecycleOwner) {
            mAdapter.setData(it ?: emptyList())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition(100L, TimeUnit.MILLISECONDS)
    }

    override fun initListener() {
        mAdapter.setOnItemClickListener { binding, item, _ ->
            val direction =
                AssetsAccountFragmentDirections.settingActionAssetsaccountfragmentToEditassetsaccountfragment(item)
            navController?.navigate(
                direction, FragmentNavigatorExtras(
                    binding.card to TRANSITION_NAME_BACKGROUND,
                    binding.sivAssetsLogo to TRANSITION_NAME_LOGO,
                    binding.tvAssetsName to TRANSITION_NAME_TITLE
                )
            )
        }

        mAdapter.setOnItemLongClickListener { _, assetsAccount, _ ->

            KVStoreHelper.read(SWITCHER_CONFIRM_BEFORE_DELETE, true).apply {
                if (this) {
                    mAssetsViewModel.deleteAssetsAccountWithDialog(requireActivity(), assetsAccount)
                } else {
                    mAssetsViewModel.deleteAssetsAccountBy(assetsAccount)
                }
            }
            true
        }


        mFooterAdapter.setOnItemClickListener { _, _, _ ->
            navController?.navigate(R.id.setting_action_assetsaccountfragment_to_editassetsaccountfragment)
        }
    }
}