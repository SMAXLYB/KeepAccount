package life.chenshi.keepaccounts.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.global.MyApplication

object ToastUtil {

    fun showShort(text: String) {
        show(text)
    }

    fun showLong(text: String) {
        show(text, false)
    }

    fun show(text: String, isShort: Boolean = true) {
        val context = MyApplication.getInstance().applicationContext
        val toastView = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
        val message =
            toastView.findViewById<TextView>(R.id.tv_toast_message).apply { this.text = text }
        val toast = Toast(context)
            .apply {
                setGravity(Gravity.CENTER, 0, 200)
                duration = if (isShort) {
                    Toast.LENGTH_SHORT
                } else {
                    Toast.LENGTH_LONG
                }
                view = toastView
            }
            .show()
    }
}