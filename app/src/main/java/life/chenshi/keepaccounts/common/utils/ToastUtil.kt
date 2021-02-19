package life.chenshi.keepaccounts.common.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.global.MyApplication

object ToastUtil {
    private var mToast: Toast? = null

    fun showSuccess(text: String) {
        show(ToastType.Success, text)
    }

    fun showFail(text: String) {
        show(ToastType.Fail, text)
    }

    fun showShort(text: String) {
        show(ToastType.Warning, text)
    }

    fun showLong(text: String) {
        show(ToastType.Warning, text, false)
    }

    fun show(type: ToastType, text: String, isShort: Boolean = true) {
        var message: TextView? = null
        val context = MyApplication.getInstance().applicationContext
        if (mToast == null) {
            val toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
            toastView.findViewById<ImageView>(R.id.iv_toast_icon)
                .setImageDrawable(ResourcesCompat.getDrawable(context.resources, type.src, null))
            message =
                toastView.findViewById<TextView>(R.id.tv_toast_message)
            mToast = Toast(context)
                .apply {
                    setGravity(Gravity.CENTER, 0, 200)
                    duration = if (isShort) {
                        Toast.LENGTH_SHORT
                    } else {
                        Toast.LENGTH_LONG
                    }
                    view = toastView
                }
        } else {
            message = mToast?.view?.findViewById(R.id.tv_toast_message)
            mToast?.view?.findViewById<ImageView>(R.id.iv_toast_icon)
                ?.setImageDrawable(ResourcesCompat.getDrawable(context.resources, type.src, null))

        }
        message?.text = text
        mToast?.show()
    }

    sealed class ToastType(@DrawableRes val src: Int) {
        object Warning : ToastType(R.drawable.toast_icon_warning)
        object Success : ToastType(R.drawable.toast_icon_success)
        object Fail : ToastType(R.drawable.toast_icon_fail)
    }
}