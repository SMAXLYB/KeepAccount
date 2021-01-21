package life.chenshi.keepaccounts.ui.setting.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    private lateinit var mBinding: FragmentBookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate<FragmentBookBinding>(
            inflater, R.layout.fragment_book, container, false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}