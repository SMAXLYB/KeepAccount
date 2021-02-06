package life.chenshi.keepaccounts.common.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.global.MyApplication

object ToastUtil {
    private var mToast: Toast? = null

    fun showShort(text: String) {
        show(text)
    }

    fun showLong(text: String) {
        show(text, false)
    }

    fun show(text: String, isShort: Boolean = true) {
        var message: TextView? = null
        if (mToast == null) {
            val context = MyApplication.getInstance().applicationContext
            val toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
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
        }
        message?.text = text
        mToast?.show()
    }
}