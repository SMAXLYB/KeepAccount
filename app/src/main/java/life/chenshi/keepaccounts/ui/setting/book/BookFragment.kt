package life.chenshi.keepaccounts.ui.setting.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.databinding.FragmentBookBinding

class BookFragment : Fragment() {

    companion object {
        private const val TAG = "BookFragment"
    }

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

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        val data = listOf(
            Book(12, "默认", "moren"),
            Book(13, "默认", "moren"),
            Book(14, "默认", "moren"),
            Book(15, "默认", "moren"),
            Book(12, "默认", "moren"),
            Book(13, "默认", "moren"),
            Book(14, "默认", "moren"),
            Book(15, "默认", "moren"),
            Book(12, "默认", "moren"),
            Book(13, "默认", "moren"),
            Book(14, "默认", "moren"),
            Book(15, "默认", "moren"),
            Book(14, "默认", "moren"),
            Book(15, "默认", "moren"),
            Book(12, "默认", "moren"),
            Book(13, "默认", "moren"),
            Book(14, "默认", "moren"),
            Book(15, "默认", "moren")
        )
        val bookAdapter = BookAdapter(data)
        mBinding.gvBooks.adapter = bookAdapter
        mBinding.gvBooks.
        mBinding.gvBooks.setOnItemClickListener { parent, view, position, id ->
            Log.d(TAG, "initListener: position=$position  id=$id")
            bookAdapter.setItemSelected(15)
        }
    }

    private fun initListener() {

        mBinding.gvBooks.setOnItemLongClickListener { parent, view, position, id ->
            Log.d(TAG, "initLongListener: position=$position  id=$id")
            true
        }
    }

    private fun initObserver() {

    }
}