package life.chenshi.keepaccounts.ui.setting.book

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import life.chenshi.keepaccounts.database.entity.Book
import life.chenshi.keepaccounts.databinding.DialogNewBookBinding

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

            mBookViewModel.insertBook(Book(name = name, description = description))
            dismiss()
        }
    }
}