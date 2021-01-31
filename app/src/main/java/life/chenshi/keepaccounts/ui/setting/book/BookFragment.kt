package life.chenshi.keepaccounts.ui.setting.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
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
        // 单击选择,只会触发本地id
        mBinding.gvBooks.setOnItemClickListener { _, _, _, id ->
            if (id == -1L) {
                // 新增, 触发数据库
                NewBookFragment().showNow(requireActivity().supportFragmentManager, "ADD_BOOK")
            } else {
                mBookViewModel.apply {
                    setCurrentBookId(id.toInt())
                }
            }
        }

        // 长按删除, 一定触发数据库, 可能触发本地id
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
            }
            true
        }
    }

    private fun initObserver() {

        // 账本数据
        mBookViewModel.booksLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            if (it.isEmpty()) {
                mBinding.gpBookEmpty.visible()
            } else {
                mBinding.gpBookEmpty.inVisible()
            }

            mBookViewModel.currentBookId.apply {
                mBookViewModel.getCurrentBookPositionInListById(value!!).let { position ->
                    mAdapter?.setItemSelected(position)
                }
            }
            mAdapter?.setData(
                it.toMutableList().apply {
                    plusAssign(Book(-1, "", ""))
                })
        }

        lifecycleScope.launch {
            mBookViewModel.currentBookId
                .observe(viewLifecycleOwner) {
                    // 如果是删除变动,数据库会刷新界面
                    if (it != -1) {
                        mBookViewModel.booksLiveData.apply {
                            value = value
                        }
                    }
                }
        }
    }
}