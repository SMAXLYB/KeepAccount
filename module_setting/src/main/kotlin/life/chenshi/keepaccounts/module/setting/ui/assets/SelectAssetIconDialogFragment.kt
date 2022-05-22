package life.chenshi.keepaccounts.module.setting.ui.assets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import life.chenshi.keepaccounts.module.common.databinding.CommonBottomSheetRecyclerviewBinding
import life.chenshi.keepaccounts.module.setting.adapter.AssetIconGroupAdapter
import life.chenshi.keepaccounts.module.setting.bean.assetIconGroup
import life.chenshi.keepaccounts.module.setting.bean.assets.*
import javax.inject.Inject

@AndroidEntryPoint
class SelectAssetIconDialogFragment : BottomSheetDialogFragment() {

    @Inject
    internal lateinit var mAdapter: AssetIconGroupAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = CommonBottomSheetRecyclerviewBinding.inflate(layoutInflater).apply {
            rvContent.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            this.rvContent.adapter = mAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.setData(
            listOf(
                assetIconGroup("网络账户", Alipay, WeChatWallet),
                assetIconGroup(
                    "资金账户", ChinaBank, AgriculturalBank, CiticBank, CommunicationsBank,
                    ConstructionBank, IndustrialAndCommercialBank, MerchantsBank, PostalSavingsBank
                ),
                assetIconGroup("实体账户", Cash),
            )
        )
    }
}