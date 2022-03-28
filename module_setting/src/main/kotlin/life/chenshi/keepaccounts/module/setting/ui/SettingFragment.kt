package life.chenshi.keepaccounts.module.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.constant.DAY_NIGHT_MODE
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentSettingBinding
import life.chenshi.keepaccounts.module.setting.vm.SettingViewModel

class SettingFragment : Fragment() {
    private lateinit var mBinding: SettingFragmentSettingBinding
    private val mSettingViewModel by viewModels<SettingViewModel>()

    companion object {
        private const val TAG = "SettingFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.setting_fragment_setting, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initView() {
        mBinding.tvSettingCurrentBook.apply {
            mSettingViewModel.hasDefaultBook({
                lifecycleScope.launch {
                    val book = mSettingViewModel.getBookNameById(it)
                    mBinding.tvSettingCurrentBook.text = book.name
                }
            }, {
                mBinding.tvSettingCurrentBook.text = ""
            })
        }

        mBinding.settingUserName.text = mSettingViewModel.getGreetContent()

        when (KVStoreHelper.read(DAY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) {
            MODE_NIGHT_YES -> {
                mBinding.switcherNightMode.reset(true)
            }
            MODE_NIGHT_NO -> {
                mBinding.switcherNightMode.reset(false)
            }
            MODE_NIGHT_FOLLOW_SYSTEM -> {
                mBinding.switcherNightMode.reset(false)
            }
        }
    }

    private fun initListener() {
        mBinding.switcherNightMode.setOnCheckChangedListener {
            if (it) {
                KVStoreHelper.write(DAY_NIGHT_MODE, MODE_NIGHT_YES)
                setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                KVStoreHelper.write(DAY_NIGHT_MODE, MODE_NIGHT_NO)
                setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
        mBinding.llSettingItemCurrentBook.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_bookActivity)
        )

        mBinding.llSettingItemCategory.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_categoryActivity)
        )

        mBinding.llSettingItemMoreSetting.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.setting_action_settingfragment_to_moresettingfragment)
        )
    }
}