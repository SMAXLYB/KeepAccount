package life.chenshi.keepaccounts.module.setting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.common.constant.APP_FIRST_USE_TIME
import life.chenshi.keepaccounts.module.common.constant.DAY_NIGHT_MODE
import life.chenshi.keepaccounts.module.common.constant.PATH_SETTING_THEME
import life.chenshi.keepaccounts.module.common.constant.navTo
import life.chenshi.keepaccounts.module.common.service.IBookRouterService
import life.chenshi.keepaccounts.module.common.service.ICategoryRouterService
import life.chenshi.keepaccounts.module.common.utils.*
import life.chenshi.keepaccounts.module.common.utils.storage.KVStoreHelper
import life.chenshi.keepaccounts.module.setting.R
import life.chenshi.keepaccounts.module.setting.databinding.SettingFragmentUserProfileBinding
import life.chenshi.keepaccounts.module.setting.vm.UserProfileViewModel

@AndroidEntryPoint
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
        initObserver()
        initListener()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mUserProfileViewModel.totalNumOfBook.collect {
                    mBinding.tvBooksNum.text = "$it"
                }
            }
            launch {
                mUserProfileViewModel.totalNumOfRecord.collect {
                    mBinding.tvRecordsNum.text = "$it"
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        StatusBarUtil.init(requireActivity())
            .addStatusBatHeightTo(mBinding.clContainer)

        var useDays = DateUtil.getDaysBetween(KVStoreHelper.read(APP_FIRST_USE_TIME), System.currentTimeMillis()) + 1
        if (useDays == 0L) {
            useDays = 1
        }

        mBinding.tvDaysNum.text = useDays.toString()
        mBinding.tvNightMode.text = when (context?.nightMode()) {
            true -> {
                mBinding.ivNightMode.setImageDrawable(
                    AppCompatResources.getDrawable(requireContext(), R.drawable.setting_icon_day)
                )
                "日间"
            }
            else -> {
                mBinding.ivNightMode.setImageDrawable(
                    AppCompatResources.getDrawable(requireContext(), R.drawable.setting_icon_night)
                )
                "夜间"
            }
        }
        mBinding.settingHelp.ivIcon.setVisibleWithDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.setting_icon_help
            )
        )
        mBinding.settingFeedback.ivIcon.setVisibleWithDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.setting_icon_feedback
            )
        )
        mBinding.settingAbout.ivIcon.setVisibleWithDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.setting_icon_about
            )
        )
        mBinding.allSetting.ivIcon.setVisibleWithDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.setting_icon_setting
            )
        )
    }

    override fun onResume() {
        super.onResume()
        getTotalNum()
    }

    private fun getTotalNum() {
        mUserProfileViewModel.getTotalNum()
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
    }
}