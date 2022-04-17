package life.chenshi.keepaccounts.module.setting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import life.chenshi.keepaccounts.module.common.constant.APP_FIRST_USE_TIME
import life.chenshi.keepaccounts.module.common.constant.DAY_NIGHT_MODE
import life.chenshi.keepaccounts.module.common.constant.PATH_SETTING_THEME
import life.chenshi.keepaccounts.module.common.constant.navTo
import life.chenshi.keepaccounts.module.common.service.IBookRouterService
import life.chenshi.keepaccounts.module.common.service.ICategoryRouterService
import life.chenshi.keepaccounts.module.common.utils.DateUtil
import life.chenshi.keepaccounts.module.common.utils.StatusBarUtil
import life.chenshi.keepaccounts.module.common.utils.nightMode
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentUserProfileBinding
import life.chenshi.keepaccounts.module.setting.vm.UserProfileViewModel

class UserProfileFragment : Fragment() {
    private lateinit var mBinding: SettingFragmentUserProfileBinding
    private val mUserProfileViewModel by viewModels<UserProfileViewModel>()

    companion object {
        private const val TAG = "SettingFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.setting_fragment_user_profile, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        StatusBarUtil.init(requireActivity())
            .addStatusBatHeightTo(mBinding.clContainer)
        // mBinding.tvSettingCurrentBook.apply {
        //     mUserProfileViewModel.hasDefaultBook({
        //         lifecycleScope.launch {
        //             val book = mUserProfileViewModel.getBookNameById(it)
        //             mBinding.tvSettingCurrentBook.text = book.name
        //         }
        //     }, {
        //         mBinding.tvSettingCurrentBook.text = ""
        //     })
        // }
        //
        // mBinding.settingUserName.text = mUserProfileViewModel.getGreetContent()
        //
        mBinding.tvDaysNum.text =
            "${
                DateUtil.getDaysBetween(
                    1000 * KVStoreHelper.read<Long>(APP_FIRST_USE_TIME), System.currentTimeMillis()
                ) + 1
            }"
        mBinding.tvNightMode.text = when (context?.nightMode()) {
            true -> {
                mBinding.ivNightMode.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.setting_icon_day)
                )
                "日间"
            }
            else -> {
                mBinding.ivNightMode.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.setting_icon_night)
                )
                "夜间"
            }
        }
    }

    private fun initListener() {
        mBinding.clPart1.setOnClickListener {
            context?.navTo<IBookRouterService>()
        }
        mBinding.clPart2.setOnClickListener {
            context?.navTo<ICategoryRouterService>()
        }

        mBinding.clPart4.setOnClickListener {
            val directions =
                UserProfileFragmentDirections.settingActionUserprofilefragmentToMainactivity(PATH_SETTING_THEME)
            findNavController().navigate(directions)
        }
        mBinding.clPart5.setOnClickListener {
            if (false == context?.nightMode()) {
                KVStoreHelper.write(DAY_NIGHT_MODE, MODE_NIGHT_YES)
                setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                KVStoreHelper.write(DAY_NIGHT_MODE, MODE_NIGHT_NO)
                setDefaultNightMode(MODE_NIGHT_NO)
            }
        }
        mBinding.allSetting.rlSetting.setOnClickListener {
            findNavController().navigate(R.id.setting_action_userprofilefragment_to_mainactivity)
        }
        // mBinding.llSettingItemCurrentBook.setOnClickListener(
        //     Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_bookActivity)
        // )
        //
        // mBinding.llSettingItemCategory.setOnClickListener(
        //     Navigation.createNavigateOnClickListener(R.id.action_settingFragment_to_categoryActivity)
        // )
        //
        // mBinding.llSettingItemMoreSetting.setOnClickListener(
        //     Navigation.createNavigateOnClickListener(R.id.setting_action_settingfragment_to_mainactivity)
        // )
    }
}