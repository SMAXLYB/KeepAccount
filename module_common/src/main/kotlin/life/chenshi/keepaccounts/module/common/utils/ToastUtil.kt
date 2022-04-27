package life.chenshi.keepaccounts.module.common.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import life.chenshi.keepaccounts.module.common.R
import life.chenshi.keepaccounts.module.common.base.BaseApplication

object ToastUtil {

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
        val context = BaseApplication.application
        val toastView = LayoutInflater.from(context).inflate(R.layout.common_layout_toast, null)
        toastView.findViewById<ImageView>(R.id.iv_toast_icon)
            .setImageDrawable(AppCompatResources.getDrawable(context, type.src))
        val message =
            toastView.findViewById<TextView>(R.id.tv_toast_message)
        message.text = text
        val mToast = Toast(context)
            .apply {
                setGravity(Gravity.CENTER, 0, 0)
                duration = if (isShort) {
                    Toast.LENGTH_SHORT
                } else {
                    Toast.LENGTH_LONG
                }
                view = toastView
            }
        mToast.show()
    }

    sealed class ToastType(@DrawableRes val src: Int) {
        object Warning : ToastType(R.drawable.common_toast_icon_warning)
        object Success : ToastType(R.drawable.common_toast_icon_success)
        object Fail : ToastType(R.drawable.common_toast_icon_fail)
    }
}