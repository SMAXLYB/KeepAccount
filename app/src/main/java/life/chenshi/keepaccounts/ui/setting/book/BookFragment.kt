package life.chenshi.keepaccounts.ui.setting.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.databinding.FragmentBookBinding
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
                NewBookFragment().showNow(requireActivity().supportFragmentManager, "ADD_BOOK")
            } else {
                mBookViewModel.currentBookId.value = id.toInt()
            }
        }
        mBinding.gvBooks.setOnItemLongClickListener { _, _, _, id ->
            mBookViewModel.takeIf { id != -1L }?.let {
                activity?.let { activity ->
                    AlertDialog.Builder(activity)
                        .setPositiveButton("确定") { _, _ ->
                            it.deleteBookById(id.toInt())
                        }
                        .setNegativeButton("取消") { _, _ -> }
                        .setMessage("确定删除吗")
                        .show()
                }
                // DataStoreUtil.writeToDataStore()
            }
            true
        }
    }

    private fun initObserver() {
        // 账本数据
        mBookViewModel.booksLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                mBinding.gpBookEmpty.visible()
            } else {
                mBinding.gpBookEmpty.inVisible()
            }
            mAdapter?.setData(it.toMutableList().apply { plusAssign(Book(-1, "", "")) })
        }

        // 当前账本
        mBookViewModel.currentBookId.observe(viewLifecycleOwner) {
            mAdapter?.setItemSelected(it)
        }
    }
}