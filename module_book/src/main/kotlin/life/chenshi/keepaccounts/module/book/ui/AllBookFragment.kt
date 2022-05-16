package life.chenshi.keepaccounts.module.book.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.book.R
import life.chenshi.keepaccounts.module.book.adapter.AllBookAdapter
import life.chenshi.keepaccounts.module.book.databinding.BookFragmentAllBookBinding
import life.chenshi.keepaccounts.module.book.ui.dialog.NewBookFragment
import life.chenshi.keepaccounts.module.book.vm.AllBookViewModel
import life.chenshi.keepaccounts.module.common.database.entity.Book
import life.chenshi.keepaccounts.module.common.utils.ToastUtil
import life.chenshi.keepaccounts.module.common.utils.inVisible
import life.chenshi.keepaccounts.module.common.utils.onBackPressed
import life.chenshi.keepaccounts.module.common.utils.visible
import life.chenshi.keepaccounts.module.common.view.CustomDialog

@AndroidEntryPoint
class AllBookFragment : Fragment() {

    companion object {
        private const val TAG = "BookFragment"
    }

    private lateinit var mBinding: BookFragmentAllBookBinding
    private val mBookViewModel by viewModels<AllBookViewModel>()
    private var mAdapter: AllBookAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.book_fragment_all_book, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        mAdapter = AllBookAdapter(listOf(Book(-1, "", "")))
        mBinding.gvBooks.adapter = mAdapter
    }

    private fun initListener() {
        mBinding.bar.setLeftClickListener {
            onBackPressed()
        }
        // 单击选择,只会触发本地id
        mBinding.gvBooks.setOnItemClickListener { _, _, _, id ->
            if (id == -1L) {
                // 新增, 触发数据库
                NewBookFragment().showNow(childFragmentManager, "ADD_BOOK")
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
                    CustomDialog.Builder(activity)
                        .setTitle("删除提示")
                        .setMessage("\u3000\u3000您正在进行删除操作, 此操作不可逆, 确定继续吗?")
                        .setPositiveButton("确定") { dialog, _ ->
                            kotlin.runCatching { it.deleteBookById(id.toInt()) }
                                .onSuccess {
                                    ToastUtil.showSuccess("删除成功")
                                    dialog.dismiss()
                                }
                                .onFailure { ToastUtil.showFail("删除失败") }
                        }
                        .setNegativeButton("取消")
                        .setCancelable(false)
                        .setClosedButtonEnable(false)
                        .build()
                        .showNow()
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

            mBookViewModel.currentBookId.value?.apply {
                mBookViewModel.getCurrentBookPositionInListById(this).let { position ->
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