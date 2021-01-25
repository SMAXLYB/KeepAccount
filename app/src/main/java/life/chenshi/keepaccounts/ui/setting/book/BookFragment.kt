package life.chenshi.keepaccounts.ui.setting.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.databinding.FragmentBookBinding
import life.chenshi.keepaccounts.utils.ToastUtil
import life.chenshi.keepaccounts.utils.inVisible
import life.chenshi.keepaccounts.utils.visible

class BookFragment : Fragment() {

    companion object {
        private const val TAG = "BookFragment"
    }

    private lateinit var mBinding: FragmentBookBinding
    private val mBookViewModel by activityViewModels<BookViewModel>()
    private var mAdapter: BookAdapter? = null

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
        mAdapter = BookAdapter(listOf(Book(-1, "", "")))
        mBinding.gvBooks.adapter = mAdapter
    }

    private fun initListener() {
        mBinding.gvBooks.setOnItemClickListener { _, _, position, id ->
            if (id == -1L) {
                ToastUtil.showShort("添加")
            } else {
                ToastUtil.showShort("$position")
            }
        }
        mBinding.gvBooks.setOnItemLongClickListener { _, _, _, id ->
            mBookViewModel.takeIf { id != -1L }?.let {
                it.deleteBookById(id.toInt())
                // DataStoreUtil.writeToDataStore()
            }
            true
        }
    }

    private fun initObserver() {
        mBookViewModel.booksLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                mBinding.gpBookEmpty.visible()
            } else {
                mBinding.gpBookEmpty.inVisible()
            }
            mAdapter?.setData(it.apply { plusAssign(Book(-1, "", "")) })
        }
    }
}