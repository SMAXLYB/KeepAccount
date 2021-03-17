package life.chenshi.keepaccounts.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var mBinding: FragmentSettingBinding
    private val mSettingViewModel by viewModels<SettingViewModel>()

    companion object {
        private const val TAG = "SettingFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate<FragmentSettingBinding>(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

    }

    private fun initListener() {
        mBinding.llSettingItemCurrentBook.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_bookActivity)
        )

        mBinding.llSettingItemCategory.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_categoryActivity)
        )

        mBinding.llSettingItemMoreSetting.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_moreSettingActivity)
        )
    }
}