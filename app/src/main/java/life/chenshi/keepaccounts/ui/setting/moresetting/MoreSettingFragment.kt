package life.chenshi.keepaccounts.ui.setting.moresetting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentMoreSettingBinding
import life.chenshi.keepaccounts.module.common.base.BaseFragment
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_CONFIRM_BEFORE_DELETE
import life.chenshi.keepaccounts.module.common.constant.SWITCHER_EXIT_APP

class MoreSettingFragment : BaseFragment() {

    private lateinit var mBinding: FragmentMoreSettingBinding
    private val mMoreSettingViewModel by viewModels<MoreSettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate<FragmentMoreSettingBinding>(
            layoutInflater,
            R.layout.fragment_more_setting,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        mMoreSettingViewModel.readFromDataStore(SWITCHER_CONFIRM_BEFORE_DELETE) {
            mBinding.switcherConfirmBeforeDelete.reset(it)
        }

        mMoreSettingViewModel.readFromDataStore(SWITCHER_EXIT_APP) {
            mBinding.switcherExitApp.reset(it)
        }
    }

    private fun initListener() {
        mBinding.switcherConfirmBeforeDelete.setOnCheckChangedListener {
            mMoreSettingViewModel.writeToDataStore(SWITCHER_CONFIRM_BEFORE_DELETE, it)
        }

        mBinding.switcherExitApp.setOnCheckChangedListener {
            mMoreSettingViewModel.writeToDataStore(SWITCHER_EXIT_APP, it)
        }
    }

    private fun initObserver() {

    }
}