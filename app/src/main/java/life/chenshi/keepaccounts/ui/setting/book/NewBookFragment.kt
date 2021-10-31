package life.chenshi.keepaccounts.ui.setting.book

import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.databinding.DialogNewBookBinding
import life.chenshi.keepaccounts.module.common.utils.ToastUtil

class NewBookFragment : DialogFragment() {
    companion object {
        private const val TAG = "NewBookFragment"
    }

    private lateinit var mBinding: DialogNewBookBinding
    private val mBookViewModel by activityViewModels<BookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = DialogNewBookBinding.inflate(layoutInflater, container, false)
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