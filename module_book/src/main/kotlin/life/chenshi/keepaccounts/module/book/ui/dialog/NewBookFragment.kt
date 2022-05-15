package life.chenshi.keepaccounts.module.book.ui.dialog

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.module.book.databinding.BookDialogNewBookBinding
import life.chenshi.keepaccounts.module.book.vm.AllBookViewModel
import life.chenshi.keepaccounts.module.common.database.entity.Book
import life.chenshi.keepaccounts.module.common.utils.ToastUtil

@AndroidEntryPoint
class NewBookFragment : DialogFragment() {
    companion object {
        private const val TAG = "NewBookFragment"
    }

    private lateinit var mBinding: BookDialogNewBookBinding
    private val mBookViewModel by viewModels<AllBookViewModel>(ownerProducer = { requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = BookDialogNewBookBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.3f)
        isCancelable = false

        mBinding.tvDialogClose.setOnClickListener {
            dismiss()
        }

        mBinding.btnDialogSubmit.setOnClickListener {
            val name = mBinding.etDialogNewBookName.text?.toString()?.trim()
            val description = mBinding.etDialogNewBookDescription.text?.toString()?.trim()
            if (name.isNullOrEmpty()) {
                mBinding.etDialogNewBookName.setBackgroundColor(Color.parseColor("#A4FFD1D1"))
                return@setOnClickListener
            }

            lifecycleScope.launch {
                kotlin.runCatching {
                    mBookViewModel.insertBook(Book(name = name, description = description))
                }.onSuccess {
                    dismiss()
                }.onFailure {
                    if (it is SQLiteConstraintException) {
                        ToastUtil.showLong("账本名重复,换一个试试吧~")
                    } else {
                        ToastUtil.showLong("添加失败, 发生未知异常!")
                    }
                }
            }
        }
    }
}