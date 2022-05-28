package life.chenshi.keepaccounts.module.common.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import life.chenshi.keepaccounts.module.common.databinding.CommonDialogCustomBinding
import life.chenshi.keepaccounts.module.common.utils.gone
import life.chenshi.keepaccounts.module.common.utils.setVisibility

typealias Listener = (DialogFragment, ViewDataBinding?) -> Unit

class CustomDialog private constructor() : DialogFragment() {

    private var params = Params()

    companion object {
        private const val TAG = "CustomDialog"
    }

    private lateinit var mBinding: CommonDialogCustomBinding
    // private val mBookViewModel by activityViewModels<BookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = CommonDialogCustomBinding.inflate(layoutInflater, container, false)
        // 内容
        params.message?.let {
            mBinding.tvDialogMessage.text = it
        } ?: mBinding.tvDialogMessage.gone()

        params.binding?.apply {
            mBinding.viewContainer.removeView(mBinding.tvDialogMessage)
            (this.root.parent as? ViewGroup)?.removeView(this.root)
            mBinding.viewContainer.addView(this.root)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setDimAmount(0.3f)

        params.apply {
            // 标题
            title?.apply {
                mBinding.tvDialogTitle.text = this
            }
        }

        // 右上角关闭按钮
        mBinding.tvDialogClose.setVisibility(params.enableClosedButton)
        mBinding.tvDialogClose.setOnClickListener {
            dismiss()
        }

        // 确定按钮
        params.positiveButtonText?.let {
            mBinding.btnDialogConfirm.apply {
                text = it
                setOnClickListener {
                    params.positiveButtonClickListener?.invoke(this@CustomDialog, params.binding)
                        ?: dismiss()
                }
            }
        }

        // 取消按钮
        params.negativeButtonText?.let {
            mBinding.btnDialogCancel.apply {
                text = it
                setOnClickListener {
                    params.negativeButtonClickListener?.invoke(this@CustomDialog, params.binding)
                        ?: dismiss()
                }
            }
        } ?: mBinding.btnDialogCancel.gone()

        isCancelable = params.cancelable
    }

    fun showNow() {
        showNow(params.fragmentManager!!, params.tag)
        params.doAfterShow?.invoke(params.binding)
    }

    /**
     *  辅助创建dialog
     */
    class Builder(activity: FragmentActivity) {

        private val params = Params()

        init {
            params.fragmentManager = activity.supportFragmentManager
        }

        fun setSupportFragmentManager(fragmentManager: FragmentManager): Builder {
            params.fragmentManager = fragmentManager
            return this
        }

        fun setTag(text: String): Builder {
            params.tag = text
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            params.cancelable = cancelable
            return this
        }

        fun setTitle(title: String): Builder {
            params.title = title
            return this
        }

        fun setPositiveButton(text: String, listener: Listener? = null): Builder {
            params.positiveButtonText = text
            params.positiveButtonClickListener = listener
            return this
        }

        fun setNegativeButton(text: String, listener: Listener? = null): Builder {
            params.negativeButtonText = text
            params.negativeButtonClickListener = listener
            return this
        }

        fun setMessage(text: String): Builder {
            params.message = text
            return this
        }

        fun setContentView(viewBinding: () -> ViewDataBinding): Builder {
            // params.contentView = view().root
            params.binding = viewBinding()
            return this
        }

        fun setClosedButtonEnable(enable: Boolean): Builder {
            params.enableClosedButton = enable
            return this
        }

        fun build(): CustomDialog {
            val dialog = CustomDialog()
            dialog.params = params
            return dialog
        }

        fun doAfterShow(work: (ViewDataBinding?) -> Unit): Builder {
            params.doAfterShow = work
            return this
        }
    }

    private class Params {
        var title: String? = null
        var enableClosedButton: Boolean = true

        // var contentView: View? = null
        var positiveButtonText: String? = null
        var positiveButtonClickListener: Listener? = null
        var negativeButtonText: String? = null
        var negativeButtonClickListener: Listener? = null
        var message: String? = null
        var tag: String = "dialog"
        var fragmentManager: FragmentManager? = null
        var cancelable: Boolean = true
        var binding: ViewDataBinding? = null
        var doAfterShow: ((ViewDataBinding?) -> Unit)? = null
    }
}