package life.chenshi.keepaccounts.ui.index

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentIndexBinding

class IndexFragment : Fragment() {
    private lateinit var mBinding: FragmentIndexBinding

    companion object {
        private const val TAG = "IndexFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate<FragmentIndexBinding>(
            inflater, R.layout.fragment_index, container, false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initListener()
    }

    private fun initListener() {
        // 新建记录
        mBinding.fabNewRecord.setOnClickListener {
            it.findNavController().navigate(R.id.action_indexFragment_to_newRecordActivity, null)
        }
    }
}