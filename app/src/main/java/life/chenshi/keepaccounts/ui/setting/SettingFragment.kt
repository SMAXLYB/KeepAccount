package life.chenshi.keepaccounts.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var mBinding: FragmentSettingBinding
    private val mSettingViewModel by viewModels<SettingViewModel>()
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
    }

    private fun initView() {
        // mBinding.tvSettingCurrentBook.apply {
        //     mSettingViewModel.hasDefaultBook({},{})
        // }
    }
}