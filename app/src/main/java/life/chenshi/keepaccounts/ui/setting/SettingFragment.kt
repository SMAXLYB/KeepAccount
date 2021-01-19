package life.chenshi.keepaccounts.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSettingBinding>(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )

        initView()

        return binding.root
    }

    private fun initView() {
        // 直装新版本: 本地和数据库都没有记录
        // 老版本升级: 本地没有记录, 数据库有记录1
        // 添加记录时查看是否有本地记录,有直接添加记录
        // 本地没有记录查询,查询数据库第一条数据
        // 数据库没有记录,提示新建  数据库有记录,写进本地
    }
}