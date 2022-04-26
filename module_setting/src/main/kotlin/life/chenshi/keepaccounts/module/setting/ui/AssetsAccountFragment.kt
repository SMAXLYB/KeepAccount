package life.chenshi.keepaccounts.module.setting.ui

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.common.base.NavBindingFragment
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.module.common.utils.navController
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountAdapter
import life.chenshi.keepaccounts.module.setting.adapter.AssetsAccountFooterAdapter
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentAssetsAccountBinding
import life.chenshi.keepaccounts.module.setting.vm.AllSettingViewModel
import life.chenshi.keepaccounts.module.setting.vm.AssetsAccountViewModel
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

    override fun initListener() {
        mAdapter.setOnItemClickListener { _, item, _ ->
            val direction = AssetsAccountFragmentDirections.settingActionAssetsaccountfragmentToEditassetsaccountfragment(item)
            navController?.navigate(direction)
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